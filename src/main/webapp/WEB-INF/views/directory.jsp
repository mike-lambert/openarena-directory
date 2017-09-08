<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
 <meta charset="utf-8" />
 <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
 <!-- Latest compiled and minified CSS -->
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"/>
 <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Coda" />
 <link href="https://fonts.googleapis.com/css?family=Jura" rel="stylesheet"/>
 <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap.min.css"/>
 <!-- Latest compiled and minified JavaScript -->
 <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"  crossorigin="anonymous"></script>
 <script src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
 <script src="https://cdn.datatables.net/1.10.13/js/dataTables.bootstrap.min.js"></script>

 <!-- self -->
 <link rel="stylesheet" href="../static/style.css"/>
 <script type="text/javascript" language="javascript">
  $(document).ready(function(){
    $('#servers').DataTable({
        "paging":   false,
        "ordering": true,
        "info":     false,
        "oLanguage": {
          "sSearch" : "Поиск"
        }
    });
  });
 </script>
 <title>Welcome to OpenArena°</title>
</head>
<body style="font-family: 'Jura', sans-serif; font-size: 15px; font-weight: bold; background-color: #000000; color: #FFFFCC; padding-top: 10px;">
 <div class="container-fluid" id="page">
  <div class="container-fluid" style="margin: 0 auto;" id="table-container">
   <div id="datatable" style="min-width: 800px; max-width: 100%;">
    <table id="servers" class="table" style="min-width: 800px; max-width: 100%; font-size: 12px;">
     <thead>
      <th>Тип</th>
      <th>Сервер</th>
      <th>Нагрузка</th>
      <th>Режим</th>
     </thead>
     <tbody>
         <c:forEach items="${servers}" var="server">
          <td>${server.game}</td>
          <td>
            <div>${server.displayName}</div>
            <div>${server.address}</div>
            <div>Map: ${server.map}</div>
            <div>Location: ${server.location}</div>
            <div>Ping: ${server.requestDuration}</div>
          </td>
          <td>
            ${server.playersPresent}/${server.slotsAvailable}
          </td>
          <td>
            ${server.gameType}
          </td>
         </c:forEach>
     </tbody>
    </table>
   </div>
  </div>
 </div>
 <div style="position: absolute; bottom: 0; font-size: 10px; font-family: Coda;">
  <div style="margin: 0 auto;">IP: ${request.getRemoteAddr()}</div>
 </div>
</body>
</html>
