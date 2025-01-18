<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Busca un libro</title>
  <link rel="stylesheet" href="style.css">

</head>
<body>
<h2>Busca un libro en la base de datos</h2>

<form action="Encuentralibro" method="get">
    <p><label for="isbn">Isbn: &nbsp</label><input type="text" id="isbn" name="isbn"></p>

    <button type="submit">Enviar</button>
</form>
</body>
</html>







