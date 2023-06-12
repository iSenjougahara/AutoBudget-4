package web;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.util.Date;
import model.Estoque;
import model.Filial;
import model.ModeloCarro;
import model.Pecas;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "ApiServlet", urlPatterns = {"/api/*"})
public class ApiServlet extends HttpServlet {

    private JSONObject getJSONBody(BufferedReader reader) throws IOException{
        StringBuilder buffer = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return new JSONObject(buffer.toString());
    }
    
    private void processSession(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(request.getMethod().toLowerCase().equals("put")){
            JSONObject body = getJSONBody(request.getReader());
            String login = body.getString("login");
            String password = body.getString("password");
            User u = User.getUser(login, password);
            if(u==null){
                response.sendError(403, "Login or password incorrects");
                file.put("error", "Login or password incorrects");
            }else{
                request.getSession().setAttribute("user", u);
                file.put("id", u.getRowId());
                file.put("login", u.getLogin());
                file.put("name", u.getName());
                file.put("role", u.getRole());
                file.put("passwordHash", u.getPasswordHash());
                //file.put("parkingHourPrice", Estoque.HOUR_PRICE);
                file.put("message", "Logged in");
            }
        }else if(request.getMethod().toLowerCase().equals("delete")){
            request.getSession().removeAttribute("user");
            file.put("message", "Logged out");
        }else if(request.getMethod().toLowerCase().equals("get")){
            if(request.getSession().getAttribute("user") == null){
                response.sendError(403, "No session");
                file.put("error", "No session");
            }else{
                User u = (User) request.getSession().getAttribute("user");
                file.put("id", u.getRowId());
                file.put("login", u.getLogin());
                file.put("name", u.getName());
                file.put("role", u.getRole());
                file.put("passwordHash", u.getPasswordHash());
                //file.put("parkingHourPrice", Estoque.HOUR_PRICE);
            }
        }else{
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }
private void processPeca(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendError(401, "Unauthorized: no session");
            file.put("error", "Unauthorized: no session");
        } else if (!user.getRole().equals("ADMIN")) {
            response.sendError(401, "Unauthorized: only admin can manage peças");
            file.put("error", "Unauthorized: only admin can manage peças");

        } else if (request.getMethod().equalsIgnoreCase("GET")) {
            if (request.getParameter("parameter") != null && !request.getParameter("parameter").isEmpty()) {
                String parameter = request.getParameter("parameter"); 

             
                int parameterValue = Integer.parseInt(parameter);

         
                file.put("list", new JSONArray(Pecas.getPecas(parameterValue)));
            } else {
         
                file.put("list", new JSONArray(Pecas.getPecas()));
            }

        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            JSONObject body = getJSONBody(request.getReader());
            String nomePeca = body.getString("login");
            double preco = body.getDouble("name");
            long modeloCarroRowId = body.getLong("role");
            Pecas.insertPecas(nomePeca, preco, modeloCarroRowId);
        } else if (request.getMethod().equalsIgnoreCase("PUT")) {
            JSONObject body = getJSONBody(request.getReader());
            long rowId = body.getLong("rowId");
            String nomePeca = body.getString("login");
            double preco = body.getDouble("name");
            long modeloCarroRowId = body.getLong("role");
            Pecas.updatePecas(rowId, nomePeca, preco, modeloCarroRowId);
        } else if (request.getMethod().equalsIgnoreCase("DELETE")) {
            Long rowId = Long.parseLong(request.getParameter("rowId"));
            Pecas.deletePecas(rowId);
        } else 
        {
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }   
}
    private void processModeloCarro(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
       /* HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendError(401, "Unauthorized: no session");
            file.put("error", "Unauthorized: no session");
        } else if (!user.getRole().equals("ADMIN")) {
            response.sendError(401, "Unauthorized: only admin can manage modelos");
            file.put("error", "Unauthorized: only admin can manage modelos"); 
        } else*/ if (request.getMethod().equalsIgnoreCase("GET")) {
            file.put("list", new JSONArray(ModeloCarro.getModeloCarros()));
        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            JSONObject body = getJSONBody(request.getReader());
            String nomeModelo = body.getString("nomeModelo");
            Date ano = new Date(body.getLong("ano"));
            String marca = body.getString("marca");
            ModeloCarro.insertModeloCarro(nomeModelo, ano, marca);
        }/*else if (request.getMethod().equalsIgnoreCase("PUT")) {
            JSONObject body = getJSONBody(request.getReader());
            long rowId = body.getLong("rowId");
            String nomeModelo = body.getString("nomeModelo");
            Date ano = new Date(body.getLong("ano"));
            String marca = body.getString("marca");
            ModeloCarro.updateModeloCarro(rowId, nomeModelo, ano, marca);
        } else if (request.getMethod().equalsIgnoreCase("DELETE")) {
            Long rowId = Long.parseLong(request.getParameter("rowId"));
            ModeloCarro.deleteModeloCarro(rowId); 
        } */ else {
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }

   /* private void processFilial(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendError(401, "Unauthorized: no session");
            file.put("error", "Unauthorized: no session");
        } else if (!user.getRole().equals("ADMIN")) {
            response.sendError(401, "Unauthorized: only admin can manage filial");
            file.put("error", "Unauthorized: only admin can manage filial");
        } else if (request.getMethod().equalsIgnoreCase("GET")) {
            file.put("list", new JSONArray(Filial.getFiliais()));
        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            JSONObject body = getJSONBody(request.getReader());
            String nomeFilial = body.getString("nomeFilial");
            String regiao = body.getString("regiao");
            String cep = body.getString("cep");
            String estado = body.getString("estado");
            Filial.insertFilial(nomeFilial, regiao, cep, estado);
        } else if (request.getMethod().equalsIgnoreCase("PUT")) {
            JSONObject body = getJSONBody(request.getReader());
            long rowId = body.getLong("rowId");
            String nomeFilial = body.getString("nomeFilial");
            String regiao = body.getString("regiao");
            String cep = body.getString("cep");
            String estado = body.getString("estado");
            Filial.updateFilial(rowId, nomeFilial, regiao, cep, estado);
        } else if (request.getMethod().equalsIgnoreCase("DELETE")) {
            Long rowId = Long.parseLong(request.getParameter("rowId"));
            Filial.deleteFilial(rowId);
        } else {
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }*/

     protected void processEstoque (JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
    
        if(request.getMethod().toLowerCase().equals("get"))
        
            file.put("list", new JSONArray(Estoque.getEstoques()));
        
        else if(request.getMethod().toLowerCase().equals("post")){
            JSONObject body = getJSONBody(request.getReader());
            String cdpeca = body.getString("cdpeca");
            //String nomepeca = body.getString("nomepeca");
            String filial = body.getString("filial");
            Integer quantidade = body.getInt("quantidade");
            Estoque.insertEstoque(cdpeca, filial, quantidade);
        }
        else if(request.getMethod().toLowerCase().equals("put")){
            JSONObject body = getJSONBody(request.getReader());
            Long rowId = body.getLong("rowid");
            String cdpeca = body.getString("cdpeca");
            String nomepeca = body.getString("nomepeca");
            String filial = body.getString("filial");
            Integer quantidade = body.getInt("quantidade");
            Estoque.updateEstoque(rowId,cdpeca, nomepeca, filial, quantidade);
        }else if(request.getMethod().toLowerCase().equals("delete")){
            Long id = Long.parseLong(request.getParameter("id"));
            Estoque.deleteEstoqueline(id);
        }else{
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }
    
       private void processUsers(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(request.getSession().getAttribute("user")==null){
            response.sendError(401, "Unauthorized: no session");
            file.put("error", "Unauthorized: no session");
        }else if(!((User)request.getSession().getAttribute("user")).getRole().equals("ADMIN")){
            response.sendError(401, "Unauthorized: only admin can manage users");
            file.put("error", "Unauthorized: only admin can manage users");
        }else if(request.getMethod().toLowerCase().equals("get")){
            file.put("list", new JSONArray(User.getUsers()));
        }else if(request.getMethod().toLowerCase().equals("post")){
            JSONObject body = getJSONBody(request.getReader());
            String login = body.getString("login");
            String name = body.getString("name");
            String role = body.getString("role");
            String password = body.getString("password");
            User.insertUser(login, name, role, password);
        }else if(request.getMethod().toLowerCase().equals("put")){
            JSONObject body = getJSONBody(request.getReader());
            String login = body.getString("login");
            String name = body.getString("name");
            String role = body.getString("role");
            String password = body.getString("password");
            User.updateUser(login, name, role, password);
        }else if(request.getMethod().toLowerCase().equals("delete")){
            Long id = Long.parseLong(request.getParameter("id"));
            User.deleteUser(id);
        }else{
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }
    
     protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject file = new JSONObject();

        try {
            if (request.getRequestURI().endsWith("/api/session")) {
                 processSession(file, request, response);
            } else if (request.getRequestURI().endsWith("/api/users")) {
                  processUsers(file, request, response);
            } else if (request.getRequestURI().endsWith("/api/peca")) {
                processPeca(file, request, response);
            } else if (request.getRequestURI().endsWith("/api/modeloCarro")) {
                processModeloCarro(file, request, response);
            }/* else if (request.getRequestURI().endsWith("/api/filial")) {
                processFilial(file, request, response); 
            }*/ else if (request.getRequestURI().endsWith("/api/estoques")) { 
                processEstoque(file, request, response);
            }  else if(request.getRequestURI().endsWith("/api/index")){
                processEstoque(file, request, response);
            } else {
                response.sendError(400, "Invalid URL");
                file.put("error", "Invalid URL");
            }
        } catch (Exception ex) {
            response.sendError(500, "Internal error: " + ex.getLocalizedMessage());
            file.put("error", "Internal error: " + ex.getLocalizedMessage());
        }

        response.getWriter().print(file.toString());
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
