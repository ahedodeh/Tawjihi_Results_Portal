<%
    String T1 = request.getParameter("T1");
    String T2 = request.getParameter("T2");
    int sum  = Integer.parseInt(T1) + Integer.parseInt(T2);
    out.println("Sum = " + sum + "\n");
%>
