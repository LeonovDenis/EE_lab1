/*
 * Пример простого сервлета
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Пример простого сервлета. В примере демонстрируется использование атрибутов
 * приложения, файлов cookie и Http-сессии.
 *
 * @author Ю.Д.Заковряшин, 2020
 * @version 1.0
 */
@WebServlet(urlPatterns = {"/Hello"})
public class NewOne extends HttpServlet {
String msg1="";
    /**
     * Метод обрабатывает HTTP-запрос <code>GET</code>. Через запрос в метод
     * передаётся строка сообщения, которая сохраняется в атрибуте приложения,
     * представленном коллекцией типа {@link java.util.ArrayList}.
     *
     * @param request ссылка на объект, представляющий запрос к серверу.
     * @param response ссылка на объект, с помощью которого формируется ответ
     * клиенту.
     * @throws ServletException выбрасывается в случае возникновения ошибки в
     * работе сервлета.
     * @throws IOException выбрасывается при возникновении ошибки ввода/вывода.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Определение типа контента ответа
        response.setContentType("text/html;charset=UTF-8");
        // Получение параметра запроса по имени - в данном примере это 
        // произвольная строка, введённая пользователем на странице index.html
        String msg = request.getParameter("msg");
        // Проверка является ли введённая строка "пустой"

        if (msg.trim().isEmpty()) {
            // Если строка "пустая", то пользователь переадресуется на 
            // стартовую страницу приложения. 
            response.sendRedirect("resp.html");
            return;
        } else {

            if (isNumeric(msg.trim())) {

                msg1 = Integer.toString(Integer.parseInt(msg) + 1);

                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html lang=\"ru\">");
                    out.println("<head>");
                    out.println("<title>Целое число!</title>");
                    out.println("<meta charset=\"UTF-8\" />");
                    out.println("</head>");
                    out.println("<body>");
                    // Вывод подтверждения о добавлении полученного сообщения msg 
                    // к коллекции сообщений.
                    out.println("<h1>Число: "+msg +" + 1 = " + msg1 + " </h1>");
                    // Вывод числа обращений к данному методу.
                    // Вывод кнопки возврата на стартовую страницу.
                    out.println(goBack());
                    out.println("</body>");
                    out.println("</html>");
                }
            } else {
                String[] k = msg.split(" ");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html lang=\"ru\">");
                    out.println("<head>");
                    out.println("<title>Предложение</title>");
                    out.println("<meta charset=\"UTF-8\" />");
                    out.println("</head>");
                    out.println("<body>");
                    // Вывод подтверждения о добавлении полученного сообщения msg 
                    // к коллекции сообщений.
                    out.println("<h1>Строка : '" + msg + "' Длина :" + k.length + " </h1>");
                    // Вывод числа обращений к данному методу.
                    // Вывод кнопки возврата на стартовую страницу.
                    out.println(goBack());
                    out.println("</body>");
                    out.println("</html>");
                }
            }

        }

        // Получение выходного потока, в который будет записываться ответ 
        // пользователю в виде HTML-документа.
    }

    /**
     * Метод обрабатывает HTTP-запрос <code>POST</code>. Метод выводит список
     * полученных сообщений пользователя, который содержится в атрибуте
     * приложения, представленном коллекцией типа {@link java.util.ArrayList}.
     *
     * @param request ссылка на объект, представляющий запрос к серверу.
     * @param response ссылка на объект, с помощью которого формируется ответ
     * клиенту.
     * @throws ServletException выбрасывается в случае возникновения ошибки в
     * работе сервлета.
     * @throws IOException выбрасывается при возникновении ошибки ввода/вывода.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Определение типа содержимого ответа и кодировки страницы
        response.setContentType("text/html;charset=UTF-8");
        // Получение ссылки на атрибут с именем list.
        Object obj = getServletContext().getAttribute("list");
        if (obj == null) {
            // Если такого атрибута нет, то пользователь переадресуется на
            // стандартную страницу ошибок с кодом завершения SC_BAD_REQUEST 
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Message list not found");
            return;
        }
        // Получение ссылки на сессию. В данном случае используется 
        // метод getSession() c параметром false, который позволяет получить
        // ссылку только на ранее открытую сессию, новая сессия создаваться не
        // не будет. Если открытой сессии нет, то метод вернёт ссылку null.
        HttpSession s = request.getSession(false);
        Integer i = 0;
        if (s == null) {
            // Если сессии нет, то пользователь переадресуется на
            // стандартную страницу ошибок с кодом завершения SC_BAD_REQUEST 
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Http-session isn't present");
            return;
        } else {
            // Получение атрибута сессии с именем count, который хранит 
            // количество вызовов данного метода.
            Object tmp = s.getAttribute("count");
            // Увеличение счётчика обращений к методу
            i = (Integer) tmp;
            ++i;
            // Обновление счётчика
            s.setAttribute("count", i);
            if (i > 3) {
                // Если счётчик превышает заданное значение, то сессия 
                // закрывается, а счётчик обнуляется.
                s.invalidate();
                i = 0;
            }
        }
        // Вызов метода, формирующего список сообщений.
        String msg = getList(obj);
        // Получение выходного потока, в который будет записываться ответ 
        // пользователю в виде HTML-документа.
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Hello</title>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Hello</h1>");
            // Вывод списка сообщений.
            out.println(msg);
            // Вывод значений счётчика обращений к данному методу.
            out.println("<p>Count: " + i + "</p>");
            // Вывод кнопки возврата на стартовую страницу.
            out.println(goBack());
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Метод возвращает краткое описание сервлета.
     *
     * @return возвращает строку с кратким описанием сервлета.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    /**
     * Метод формирует html-код, содержащий список полученных сообщений.
     *
     * @param obj ссылка на атрибут приложения, содержащего список сообщений.
     * @return возвращает строку, содержащую html-код с содержимым списка.
     */
    private String getList(Object obj) {
        StringBuilder sb = new StringBuilder();
        // Заголовок списка
        sb.append("<h2>Message list</h2>\n<hr>\n<ol>\n");
        ArrayList<String> lst = (ArrayList<String>) obj;
        for (String t : lst) {
            // Вывод элементов списка.
            sb.append("<li>").append(t).append("</li>\n");
        }
        // Закрытие списка.
        sb.append("</ol>");
        return sb.toString();
    }

    private String goBack() {
        return "<form action=\"index.html\">\n"
                + "            <input type=\"submit\" value=\"Back\"/>\n"
                + "        </form>";
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
}
