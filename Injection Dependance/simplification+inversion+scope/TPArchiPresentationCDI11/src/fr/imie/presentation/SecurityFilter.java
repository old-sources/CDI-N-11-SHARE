package fr.imie.presentation;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.imie.DTO.UsagerDTO;
import fr.imie.presentation.sessionBean.ConnectionSessionBean;
import fr.imie.presentation.sessionBean.SecurityAskedRessourceSessionBean;

/**
 * Servlet Filter implementation class SecurityFilter
 */
@WebFilter("/*")
public class SecurityFilter implements Filter {

	@Inject
	private ConnectionSessionBean connectionSessionBean;
	@Inject
	private SecurityAskedRessourceSessionBean askedRessourceSessionBean;

	/**
	 * Default constructor.
	 */
	public SecurityFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// UsagerDTO connectedUsager = (UsagerDTO) httpRequest.getSession()
		// .getAttribute("connectedUsager");
		UsagerDTO connectedUsager = connectionSessionBean.getConnectedUsager();
		Boolean securedResource = true;
		String url = httpRequest.getRequestURI();
		if (httpRequest.getQueryString() != null) {
			url = url.concat("?").concat(httpRequest.getQueryString());
		}

		if (url.contains("login")) {
			securedResource = false;
		}
		if (url.contains("css")) {
			securedResource = false;
		}
		if (url.contains("png")) {
			securedResource = false;
		}

		if (securedResource == false || connectedUsager != null) {
			chain.doFilter(request, response);
		} else {
			askedRessourceSessionBean.setAskedRessource(url);
			// httpRequest.getSession().setAttribute("askedResource", url);
			httpResponse.sendRedirect("login");
		}

		// UsagerDTO recentConnectedUsager = (UsagerDTO)
		// httpRequest.getSession()
		// .getAttribute("connectedUsager");
		UsagerDTO recentConnectedUsager = connectionSessionBean
				.getConnectedUsager();
		if (connectedUsager == null && recentConnectedUsager != null) {

//			String askedResource = (String) httpRequest.getSession()
//					.getAttribute("askedResource");
			String askedResource = askedRessourceSessionBean.getAskedRessource();
			if (askedResource != null) {
				httpResponse.sendRedirect(askedResource);
			} else {
				httpResponse.sendRedirect("home");
			}
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
