<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="structs.RequestData, structs.HitResult, java.util.concurrent.LinkedBlockingDeque, java.util.Deque, java.util.Locale" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Лаба 1</title>
    <script defer src="create_board.js"></script>
    <script defer src="index.js"></script>
    <script type="text/javascript" charset="UTF-8"
 src="https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraphsrc.js"></script>
 <script
  src="https://code.jquery.com/jquery-3.7.1.js"
  integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
  crossorigin="anonymous"></script>

<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraph.css" />
<link rel="stylesheet" type="text/css" href="index.css" />

</head>
<body>
    <header>Григорьев Давид Р3215 </header>
    <div class="main-holder">

        <section class="input-section">
            <form name="main" id="main-form">
                <p>Введите значение X</p>
        
                <select id="form-x-input" name="x" required>
                    <option value="">Выберите значение X</option>
                    <option value="-2.0"> -2.0</option>
                    <option value="-1.5"> -1.5</option>
                    <option value="-1.0"> -1.0</option>
                    <option value="-0.5"> -0.5</option>
                    <option value="0.0"> 0.0</option>
                    <option value="0.5"> 0.5</option>
                    <option value="1.0"> 1.0</option>
                    <option value="1.5"> 1.5</option>
                    <option value="2.0"> 2.0</option>
                </select>
        
                <p>Введите значение Y </p>
        
                <input id="form-y-input" 
                    name="y" 
                    placeholder="-3...3"
                    type="text" 
                    required
                    pattern="^-?\d+([\.,]\d+)?$"
                    >
        
                <p>Введите значение R </p>
        
                <div>
                    <input class="form-r-buttons" value="1" type="button">
                    <input class="form-r-buttons" value="2" type="button">
                    <input class="form-r-buttons" value="3" type="button">
                    <input class="form-r-buttons" value="4" type="button">
                    <input class="form-r-buttons" value="5" type="button">
                </div>
        
                <input id="form-r-input" 
                    value="" 
                    id="form-r-input" 
                    name="r"
                    tabindex="-1"
                    style="pointer-events: none;"
                    required>
        
                </br>
                <input class="centered" value="Запустить ракеты" type="button" id="submit-button">
            </form>
        </section>
        <div id="jxgbox" class="jxgbox" style="width:20rem; height:20rem;"></div>

        <footer>
            <table id="table">
                <tr>
                    <td>Значение X</td>
                    <td>Значение Y</td>
                    <td>Значение R</td>
                    <td>Попадание</td>
                    <td>Время выполнения (миллисекунды)</td>
                    <td>Дата выполнения</td>
                    <%
                        Deque<HitResult> collection = (Deque<HitResult>) request.getServletContext().getAttribute("globalHitHistoryDeque");

                        // HitResult test = new HitResult();

                        if (collection != null) {
                            for (HitResult data : collection) {
                    %>
                        <tr>
                            <td><%= "%.1f".formatted(data.getRequestData().getX()) %></td>
                            <td><%= "%.3f".formatted(data.getRequestData().getY()) %></td>
                            <td><%= "%d".formatted(data.getRequestData().getR()) %></td>
                            <td><%= data.isHit() %></td>
                            <td><%= data.getDurationMilliSeconds() %></td>
                            <td><%= data.getServerTime() %></td>
                        </tr>
                    <%
                        }
                    }
                    %>
                </tr>
            </table>
        </footer>
    </div>
</body>
</html>