<%
String app = request.getContextPath();
session.invalidate();
response.sendRedirect(app + "/sgm/Index");
%>