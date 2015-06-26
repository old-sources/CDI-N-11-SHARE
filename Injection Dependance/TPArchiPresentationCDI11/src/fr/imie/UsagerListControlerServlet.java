package fr.imie;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.imie.DTO.UsagerDTO;
import fr.imie.factory.IFactory;
import fr.imie.usages.IUsagesService;
import fr.imie.usages.UsagesService;

/**
 * Servlet implementation class UsagersServletControler
 */
@WebServlet("/usagerList")
public class UsagerListControlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private UsagesService usagesService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UsagerListControlerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		List<UsagerDTO> dtos = usagesService.readAll();

		HttpSession session = request.getSession();
		session.setAttribute("usagers", dtos);

		RequestDispatcher requestDispatcher = request
				.getRequestDispatcher("/WEB-INF/usagerList.jsp");
		requestDispatcher.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("delete") != null) {
			String numLigne = request.getParameter("numLigne");
			Integer numLigneInteger = Integer.valueOf(numLigne);
			List<UsagerDTO> usagers = (List<UsagerDTO>) request.getSession()
					.getAttribute("usagers");
			UsagerDTO dto = usagers.get(numLigneInteger - 1);
			try {
				usagesService.delete(dto);
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}

			// 1ere solution : modifier la session et afficher la vue
			request.getSession().setAttribute("usagers", usagesService.readAll());
			request.getRequestDispatcher("/WEB-INF/usagerList.jsp").forward(request,
			 response);

			// 2eme solution : repasser par le get du controller : redirect
			//response.sendRedirect("usagerList");
		} else if (request.getParameter("edit") != null) {
			String numLigne = request.getParameter("numLigne");
			Integer numLigneInteger = Integer.valueOf(numLigne);
			List<UsagerDTO> usagers = (List<UsagerDTO>) request.getSession()
					.getAttribute("usagers");
			UsagerDTO dto = usagers.get(numLigneInteger - 1);
			// UsagerDAO usagerDAO = new UsagerDAO();
			response.sendRedirect(String.format("usagerForm?numLigne=%d",
					numLigneInteger));

		} else if (request.getParameter("create") != null) {
			response.sendRedirect("usagerForm");
		}
	}

}
