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
package br.com.linkcom.sgm.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.orm.hibernate3.HibernateSystemException;

import br.com.linkcom.neo.authorization.Permission;
import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.authorization.impl.AuthorizationDAOHibernate;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.sgm.beans.Papel;
import br.com.linkcom.sgm.beans.Permissao;
import br.com.linkcom.sgm.beans.Tela;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.UsuarioPapel;
import br.com.linkcom.sgm.quartzjobs.AtualizaLancamentoResultadosJOB;
import br.com.linkcom.sgm.quartzjobs.AtualizaStatusAnomaliaJOB;
import br.com.linkcom.sgm.quartzjobs.EnviaEmailLembreteCriacaoIndicadoresJOB;
import br.com.linkcom.sgm.quartzjobs.EnviaEmailLembreteLancamentoResultadosJOB;
import br.com.linkcom.sgm.quartzjobs.EnviaEmailLembreteTratamentoAnomaliaJOB;
import br.com.linkcom.sgm.util.GeplanesUtils;


public class GeplanesAuthorizationDAO extends AuthorizationDAOHibernate {
	
	private static final Log log = LogFactory.getLog(GeplanesAuthorizationDAO.class);

	static final String usuarioClass = "usuario";
	static final String usuarioLoginField = "login";

	public User findUserByLogin(String login) {

		// Inicializa o agendador
		initScheduler();		
		
		return new QueryBuilder<User>(getHibernateTemplate())
				.select("usuario.id,usuario.nome,usuario.cargo,usuario.infComplementar,usuario.login,usuario.senha,usuario.email,usuario.bloqueado," +
						"usuariosUnidadeGerencial.id,usuariosUnidadeGerencial.funcao," +
						"unidadeGerencial.id,unidadeGerencial.nome,unidadeGerencial.sigla,unidadeGerencial.areaQualidade,unidadeGerencial.areaAuditoriaInterna," +
						"subordinacao.id," +
						"nivelHierarquico.id, nivelHierarquico.descricao, " +
						"planoGestao.id,planoGestao.anoExercicio")
				.from(Usuario.class)
				.leftOuterJoin("usuario.usuariosUnidadeGerencial usuariosUnidadeGerencial")
				.leftOuterJoin("usuariosUnidadeGerencial.unidadeGerencial unidadeGerencial")
				.leftOuterJoin("unidadeGerencial.nivelHierarquico nivelHierarquico")
				.leftOuterJoin("unidadeGerencial.subordinacao subordinacao")
				.leftOuterJoin("unidadeGerencial.planoGestao planoGestao")
				.where("usuario.login = ?", login)				
				.where("usuario.bloqueado = ?", Boolean.FALSE)				
				.unique();
	
	}

    public Role[] findUserRoles(User user) {
        List<Role> lista = new QueryBuilder<Role>(getHibernateTemplate())
				.select("papel")
				.from(UsuarioPapel.class)
				.join("usuarioPapel.papel papel")
				.where("usuarioPapel.usuario = ?", user)
				.list();
        return lista.toArray(new Role[lista.size()]);
	}

	public Permission findPermission(Role role, String controlName) {
		if (role instanceof Papel) {
			Papel papel = (Papel) role;
			if (papel.getId() == null) {
				return null;
			}
		}
		try {
            return new QueryBuilder<Permission>(getHibernateTemplate())
                        .from(Permissao.class)
                        .joinFetch("permissao.papel papel")
                        .joinFetch("permissao.tela tela")
                        .where("tela.path = ?", controlName)
                        .where("papel = ?", role)
                        .unique();
        } catch (HibernateSystemException e) {
            log.error("Erro: Existe mais de uma tela cadastrada com o caminho \"" + controlName + "\".", e);
            throw e;
        }
	}


	public Permission savePermission(String controlName, Role role, Map<String, String> permissionMap) {        
		Permissao permissao;
		Tela tela = new QueryBuilder<Tela>(getHibernateTemplate())
						.from(Tela.class)
						.where("tela.path = ?", controlName)
						.unique();
		if(tela == null){
			tela = new Tela();
			tela.setPath(controlName);
			if (controlName.contains("/")) {
				tela.setNome(controlName.substring(controlName.lastIndexOf('/') + 1));
			} else {
				tela.setNome(controlName);
			}
			hibernateTemplate.save(tela);
		}
		{
			//verificar se já existe essa permissao no banco
			permissao = new QueryBuilder<Permissao>(getHibernateTemplate())
					.from(Permissao.class)
					.where("permissao.tela.path = ?", controlName)
					.where("permissao.papel = ?", role)
					.unique();
		}
		if(permissao == null){
			//criar a permissao
			permissao = new Permissao();
			Papel papel = (Papel) role;
			permissao.setPapel(papel);
			permissao.setTela(tela);
			permissao.setPermissionMap(permissionMap);
			hibernateTemplate.save(permissao);
		} else {
			//atualizar a permissao
			Papel papel = (Papel) role;
			permissao.setPapel(papel);
			permissao.setTela(tela);
			permissao.setPermissionMap(permissionMap);
			hibernateTemplate.update(permissao);
		}
		return permissao;
    }
	
	@SuppressWarnings("unused")
	private void initScheduler() {
		final String GEPLANES_SCHED = "GEPLANES_SCHED";
		
		// Inicia scheduler se existente em cache
		Scheduler scheduler = (Scheduler) NeoWeb.getApplicationContext().getAttribute(GEPLANES_SCHED);
		try {
			//Caso nao existe, cria
			if (scheduler == null) {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
				scheduler.start();
				
				String applicationPath = GeplanesUtils.getApplicationPath(NeoWeb.getRequestContext().getServletRequest());
			
				// Job que envia email aos responsáveis das unidades gerenciais alertando para a data limite da criação de indicadores
				// A verificação é feita diariamente à 01:00h				
				JobDetail jobCriacaoIndicadores = new JobDetail("Job1", Scheduler.DEFAULT_GROUP, EnviaEmailLembreteCriacaoIndicadoresJOB.class);			
				jobCriacaoIndicadores.getJobDataMap().put("applicationPath", applicationPath);
				CronTrigger cronTrigger1 = new CronTrigger("trigger1", Scheduler.DEFAULT_GROUP, "0 0 1 * * ?");
				scheduler.scheduleJob(jobCriacaoIndicadores,cronTrigger1);
				
				// Job que envia email aos responsáveis das unidades gerenciais alertando para o lançamento de resultados pendentes
				// A verificação é feita diariamente à 01:10h
				JobDetail jobLancamentoValoresReais = new JobDetail("Job2", Scheduler.DEFAULT_GROUP, EnviaEmailLembreteLancamentoResultadosJOB.class);			
				jobLancamentoValoresReais.getJobDataMap().put("applicationPath", applicationPath);
				CronTrigger cronTrigger2 = new CronTrigger("trigger2", Scheduler.DEFAULT_GROUP, "0 10 1 * * ?");
				scheduler.scheduleJob(jobLancamentoValoresReais,cronTrigger2);
				
				// Job que envia email alertando ao responsáveis o não tratamento de determinada anomalia
				// A verificação é feita diariamente à 01:20h				
				JobDetail jobTratamentoAnomalia = new JobDetail("Job3", Scheduler.DEFAULT_GROUP, EnviaEmailLembreteTratamentoAnomaliaJOB.class);			
				jobTratamentoAnomalia.getJobDataMap().put("applicationPath", applicationPath);
				CronTrigger cronTrigger3 = new CronTrigger("trigger3", Scheduler.DEFAULT_GROUP, "0 20 1 * * ?");
				scheduler.scheduleJob(jobTratamentoAnomalia,cronTrigger3);
				
				// Job que atualiza os status das anomalias
				// A atualização é feita diariamente à 01:30h				
				JobDetail jobAtualizacaoStatusAnomalia = new JobDetail("Job4", Scheduler.DEFAULT_GROUP, AtualizaStatusAnomaliaJOB.class);			
				CronTrigger cronTrigger4 = new CronTrigger("trigger4", Scheduler.DEFAULT_GROUP, "0 30 1 * * ?");
				scheduler.scheduleJob(jobAtualizacaoStatusAnomalia,cronTrigger4);				
				
				// Job que atualiza os valores dos resultados não lançados para 0, caso a data limite para lançamento esteja expirada.
				// A atualização é feita diariamente à 01:40h				
				JobDetail jobAtualizaLancamentoResultados = new JobDetail("Job5", Scheduler.DEFAULT_GROUP, AtualizaLancamentoResultadosJOB.class);			
				CronTrigger cronTrigger5 = new CronTrigger("trigger5", Scheduler.DEFAULT_GROUP, "0 40 1 * * ?");
				scheduler.scheduleJob(jobAtualizaLancamentoResultados,cronTrigger5);				
				
				//Cache
				NeoWeb.getApplicationContext().setAttribute(GEPLANES_SCHED, scheduler);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 		
	}
}
