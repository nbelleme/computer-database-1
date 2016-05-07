package com.excilys.cdb.servlets;

import java.io.IOException;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.service.impl.CompanyService;
import com.excilys.cdb.service.impl.ComputerService;
import com.excilys.cdb.validation.Validator;

/**
 * Servlet implementation class ComputerFormServlet.
 */
@WebServlet("/ComputerFormServlet")
public class ComputerAddServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDeleteServlet.class);

    private static final long serialVersionUID = 1L;

    private final IComputerService computerService = ComputerService.getInstance();

    private final ICompanyService companyService = CompanyService.getInstance();

    private final ComputerMapper computerMapper = ComputerMapper.getInstance();

    private final CompanyMapper companyMapper = CompanyMapper.getInstance();

    private final Validator validator = Validator.getInstance();

    /**
     * Display the form to create a new computer.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.debug("Entering doGet()");

        List<CompanyDTO> companies = this.companyMapper.map(this.companyService.getCompanies());

        request.setAttribute("companies", companies);

        request.getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);

        LOGGER.debug("Exiting doGet()");

    }

    /**
     * Create a new computer.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.debug("Entering doPost()");

        ComputerDTO computer = this.computerMapper.map(request);

        Set<String> errors = this.validator.validateComputerDTO(computer);

        if (errors.isEmpty()) {
            this.computerService.createComputer(this.computerMapper.fromDTO(computer));
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {

            request.setAttribute("computer", computer);
            request.setAttribute("errors", errors);

            this.doGet(request, response);
        }

        LOGGER.debug("Exiting doPost()");
    }
}
