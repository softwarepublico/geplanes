/* 
		Copyright 2007,2008,2009,2010 da Linkcom Informática Ltda
		
		Este arquivo é parte do programa GEPLANES.
 	   
 	    O GEPLANES é software livre; você pode redistribuí-lo e/ou 
		modificá-lo sob os termos da Licença Pública Geral GNU, conforme
 	    publicada pela Free Software Foundation; tanto a versão 2 da 
		Licença como (a seu critério) qualquer versão mais nova.
 	
 	    Este programa é distribuído na expectativa de ser útil, mas SEM 
		QUALQUER GARANTIA; sem mesmo a garantia implícita de 
		COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM PARTICULAR. 
		Consulte a Licença Pública Geral GNU para obter mais detalhes.
 	 
 	    Você deve ter recebido uma cópia da Licença Pública Geral GNU  	    
		junto com este programa; se não, escreva para a Free Software 
		Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
		02111-1307, USA.
		
*/
package br.com.linkcom.sgm.controller.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.BindException;

import br.com.linkcom.neo.authorization.AuthorizationDAO;
import br.com.linkcom.neo.authorization.AuthorizationItem;
import br.com.linkcom.neo.authorization.AuthorizationModule;
import br.com.linkcom.neo.authorization.AuthorizationProcessFilter;
import br.com.linkcom.neo.authorization.AuthorizationProcessItemFilter;
import br.com.linkcom.neo.authorization.HasAccessAuthorizationModule;
import br.com.linkcom.neo.authorization.Permission;
import br.com.linkcom.neo.authorization.PermissionLocator;
import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.ControlMapping;
import br.com.linkcom.neo.controller.ControlMappingLocator;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.core.standard.ApplicationContext;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.neo.view.menu.MenuTag;
import br.com.linkcom.sgm.beans.Papel;
import br.com.linkcom.sgm.filtro.PapelFiltro;
import br.com.linkcom.sgm.service.PapelService;
import br.com.linkcom.sgm.service.TelaService;
import br.com.linkcom.sgm.util.comparator.AuthorizationProcessItemFilterComparator;


@Controller(path="/sgm/crud/Papel", authorizationModule=CrudAuthorizationModule.class)
public class PapelCrud extends CrudController<PapelFiltro, Papel, Papel> {

	private PapelService papelService;
	private TelaService telaService;
	private AuthorizationDAO authorizationDAO;
	private TransactionTemplate transactionTemplate;
	
	public void setPapelService(PapelService papelService) {
		this.papelService = papelService;
	}
	
	public void setTelaService(TelaService telaService) {
		this.telaService = telaService;
	}
	
	public void setAuthorizationDAO(AuthorizationDAO authorizationDAO) {
		this.authorizationDAO = authorizationDAO;
	}
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	@Override
	protected void entrada(WebRequestContext request, Papel form) throws Exception {
		
		telaService.refreshTelaDescriptions();
		
		AuthorizationProcessFilter authorizationFilter = new AuthorizationProcessFilter();
		authorizationFilter.setRole(form);
		preencheFiltro(request, authorizationFilter);
		
		Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap = authorizationFilter.getGroupAuthorizationMap();
		if (groupAuthorizationMap != null) {
			Set<String> keySet = groupAuthorizationMap.keySet();
			for (String tipoTela : keySet) {
				List<AuthorizationProcessItemFilter> list = groupAuthorizationMap.get(tipoTela);
				for (AuthorizationProcessItemFilter authorizationProcessItemFilter : list) {
					authorizationProcessItemFilter.setDescription(translatePath(authorizationProcessItemFilter.getPath()));
				}
				Collections.sort(list, new AuthorizationProcessItemFilterComparator());
			}
		}
		
		form.setGroupAuthorizationMap(groupAuthorizationMap);
		
		super.entrada(request, form);
	}
	
	@Override
	protected void validateBean(Papel bean, BindException errors) {

		String beanNome = (Util.strings.tiraAcento(bean.getNome())).trim();
		
		List<Papel> listaPapel = papelService.findAll();
		
		/*** verifica se o nome utilizado já foi utilizado em outro papel cadastrado ***/
		if (listaPapel != null) {
			for (Papel papel : listaPapel) {
				String papelNome  = (Util.strings.tiraAcento(papel.getNome())).trim();
				if(!papel.getId().equals(bean.getId()) && beanNome.equalsIgnoreCase(papelNome)) {
					errors.reject("","Já existe um nível de acesso com este nome.");
				}
			}
		}
		super.validateBean(bean, errors);
	}
	
	@Override
	protected void salvar(WebRequestContext request, Papel bean) throws Exception {
		super.salvar(request, bean);
		
		if (!Boolean.TRUE.equals(bean.getAdministrador())) {
			AuthorizationProcessFilter authorizationFilter = new AuthorizationProcessFilter();
			authorizationFilter.setRole(bean);
			authorizationFilter.setGroupAuthorizationMap(bean.getGroupAuthorizationMap());
			
			salvaAutorizacaoAcesso(request, authorizationFilter);
		}
		
		request.addMessage("Nível de acesso salvo com sucesso", MessageType.INFO);
	}
	
	private void preencheFiltro(WebRequestContext request, AuthorizationProcessFilter authorizationFilter) {
		authorizationFilter.setGroupAuthorizationMap(new HashMap<String, List<AuthorizationProcessItemFilter>>());
		request.setAttribute("roles", authorizationDAO.findAllRoles());
		request.setAttribute("filtro", authorizationFilter);
		
		if(authorizationFilter.getRole() != null){
			Map<String, AuthorizationModule> mapaGroupModule = new HashMap<String, AuthorizationModule>();
			Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap = authorizationFilter.getGroupAuthorizationMap();
			
			Class<?>[] controllerClasses = findControllerClasses(request.getWebApplicationContext());
			for (Class<?> controllerClass : controllerClasses) {
				Controller controller = controllerClass.getAnnotation(Controller.class);
				try {
					AuthorizationModule authorizationModule = controller.authorizationModule().newInstance();
					mapaGroupModule.put(authorizationModule.getAuthorizationGroupName(), authorizationModule);
					if(!(authorizationModule instanceof HasAccessAuthorizationModule)){
						AuthorizationProcessItemFilter[] authorizationProcessItemFilters = getAuthorizationProcessItemFilter(authorizationFilter.getRole(), controller, authorizationModule);
						for (AuthorizationProcessItemFilter authorizationItemFilter : authorizationProcessItemFilters) {
							List<AuthorizationProcessItemFilter> list = getAuthorizationListForModule(groupAuthorizationMap, authorizationModule);
							list.add(authorizationItemFilter);													
						}

					}
				} catch (InstantiationException e) {
					throw new RuntimeException("Não foi possível instanciar o módulo de autorização", e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Não foi possível instanciar o módulo de autorização", e);
				}
			}
			request.setAttribute("mapaGroupModule", mapaGroupModule);
		}
		request.setAttribute("authorizationProcessItemFilterClass", AuthorizationProcessItemFilter.class);
	}
	
	private Class<?>[] findControllerClasses(ApplicationContext applicationContext) {
		return applicationContext.getClassManager().getClassesWithAnnotation(Controller.class);
	}
	
	protected List<AuthorizationProcessItemFilter> getAuthorizationListForModule(Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap, AuthorizationModule authorizationModule) {
		String authorizationGroupName = authorizationModule.getAuthorizationGroupName();
		List<AuthorizationProcessItemFilter> list = groupAuthorizationMap.get(authorizationGroupName);
		if(list == null){
			list = new ArrayList<AuthorizationProcessItemFilter>();
			groupAuthorizationMap.put(authorizationGroupName, list);
		}
		return list;
	}

	protected AuthorizationProcessItemFilter[] getAuthorizationProcessItemFilter(Role role, Controller controller, AuthorizationModule authorizationModule) {
		String[] paths = controller.path();
		List<AuthorizationProcessItemFilter> authorizationItemFilters = new ArrayList<AuthorizationProcessItemFilter>();
		for (String path : paths) {
			Permission permission = authorizationDAO.findPermission(role, path);
			AuthorizationProcessItemFilter authorizationItemFilter = new AuthorizationProcessItemFilter();
			authorizationItemFilter.setAuthorizationModule(authorizationModule);
			authorizationItemFilter.setDescription(translatePath(path));
			authorizationItemFilter.setPath(path);
			if (permission == null) {			
				authorizationItemFilter.setPermissionMap(getDefaultPermissionMap(authorizationModule));
			}
			else {
				authorizationItemFilter.setPermissionMap(permission.getPermissionmap());
			}
			authorizationItemFilters.add(authorizationItemFilter);
		}
		return authorizationItemFilters.toArray(new AuthorizationProcessItemFilter[authorizationItemFilters.size()]);
	}
	
	private Map<String, String> getDefaultPermissionMap(AuthorizationModule authorizationModule) {
		AuthorizationItem[] authorizationItens = authorizationModule.getAuthorizationItens();
		Map<String, String> defaultPermissionMap = new HashMap<String, String>();
		
		for (AuthorizationItem item : authorizationItens) {
			String id = item.getId();
			if(item.getValores()== null || item.getValores().length == 0) throw new IllegalArgumentException("Os valores possíveis de um item de autorização não pode ser um array vazio ou null");
			String valorMaisRestritivo = item.getValores()[item.getValores().length-1];
			defaultPermissionMap.put(id, valorMaisRestritivo);
		}
		return defaultPermissionMap;
	}	
	
	private String translatePath(String string) {
		return telaService.getTelaDescriptionByUrl(string);
	}
	
	private void salvaAutorizacaoAcesso(final WebRequestContext request, final AuthorizationProcessFilter authorizationFilter) {
		final Role role = authorizationFilter.getRole();
		if (role != null) {
			PermissionLocator permissionLocator = request.getWebApplicationContext().getConfig().getPermissionLocator();
			synchronized (permissionLocator) {
				permissionLocator.clearCache();
				Collection<List<AuthorizationProcessItemFilter>> values = authorizationFilter.getGroupAuthorizationMap().values();
				final List<AuthorizationProcessItemFilter> authorizationItemFilters = new ArrayList<AuthorizationProcessItemFilter>();
				for (List<AuthorizationProcessItemFilter> value : values) {
					authorizationItemFilters.addAll(value);
				}
				transactionTemplate.execute(new TransactionCallback(){

					public Object doInTransaction(TransactionStatus status) {
						ControlMappingLocator controlMappingLocator = request.getWebApplicationContext().getConfig().getControlMappingLocator();
						for (AuthorizationProcessItemFilter filter : authorizationItemFilters) {
							ControlMapping controlMapping = controlMappingLocator.getControlMapping(filter.getPath());
							AuthorizationModule authorizationModule = controlMapping.getAuthorizationModule();
							Map<String, String> defaultPermissionMap = getDefaultPermissionMap(authorizationModule);
							Map<String, String> permissionMap = filter.getPermissionMap();
							Set<String> defaultKeySet = defaultPermissionMap.keySet();
							for (String string : defaultKeySet) {
								if(permissionMap.get(string) == null){
									permissionMap.put(string, defaultPermissionMap.get(string));
								}
							}
							authorizationDAO.savePermission(filter.getPath(), role, permissionMap);
						}
						return null;
					}});

			}
		}
		// Reseta os menus
		request.getSession().setAttribute(MenuTag.MENU_CACHE_MAP, null);
	}	
}
