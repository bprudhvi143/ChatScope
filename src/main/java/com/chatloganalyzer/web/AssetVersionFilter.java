package com.chatloganalyzer.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Gives the existing JSP views cache-safe asset URLs without changing their
 * route contracts. It also uses the packaged Chart.js library instead of a
 * third-party CDN so dashboard charts work in offline/restricted browsers.
 */
@WebFilter("/*")
public class AssetVersionFilter implements Filter {
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest httpRequest)
                || !(response instanceof HttpServletResponse httpResponse)
                || httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/assets/")) {
            chain.doFilter(request, response);
            return;
        }

        HtmlResponseWrapper captured = new HtmlResponseWrapper(httpResponse);
        chain.doFilter(request, captured);
        captured.flushBuffer();

        String contentType = httpResponse.getContentType();
        if (contentType == null || !contentType.contains("text/html")) {
            httpResponse.getWriter().write(captured.body());
            return;
        }

        String contextPath = httpRequest.getContextPath();
        String html = captured.body()
                .replace("https://cdn.jsdelivr.net/npm/chart.js", contextPath + "/assets/chart.umd.min.js?v=1")
                .replace(contextPath + "/assets/charts.js\"", contextPath + "/assets/charts.js?v=4\"")
                .replace(contextPath + "/assets/app.js\"", contextPath + "/assets/app.js?v=5\"")
                .replace(contextPath + "/assets/app.css\"", contextPath + "/assets/app.css?v=4\"");
        byte[] output = html.getBytes(StandardCharsets.UTF_8);
        httpResponse.setContentLength(output.length);
        httpResponse.getWriter().write(html);
    }

    private static final class HtmlResponseWrapper extends HttpServletResponseWrapper {
        private final CharArrayWriter buffer = new CharArrayWriter();
        private final PrintWriter writer = new PrintWriter(buffer);

        private HtmlResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() {
            return writer;
        }

        @Override
        public ServletOutputStream getOutputStream() {
            throw new IllegalStateException("HTML responses must use a writer");
        }

        @Override
        public void flushBuffer() {
            writer.flush();
        }

        private String body() {
            return buffer.toString();
        }
    }
}
