<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Elimina un libro</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h2>Elimina un libro de la base de datos</h2>

<form action="EliminaLibro" method="post">
    <p><label for="isbn">Isbn: &nbsp</label><input type="text" id="isbn" name="isbn"></p>
    <%
        String mensaje = (String)request.getAttribute("noExisteLibro");

        if (mensaje != null){
    %>
    <p class="error"><%=mensaje%></p>
    <%
        }
    %>
    <button type="submit">Enviar</button>
</form>
</body>
</html>
