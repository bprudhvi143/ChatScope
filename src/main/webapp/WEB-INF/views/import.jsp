<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.chatloganalyzer.util.ViewUtil" %>
<% String pastedTitle=request.getParameter("conversationTitle"); String pastedSource=request.getParameter("sourceType"); String pastedContent=request.getParameter("pastedContent"); %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
  <title>Import chat · ChatScope</title>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
<div class="app-shell">
  <aside class="sidebar">
    <a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark">⌁</span><span>chat<span>scope</span><small>ANALYZER</small></span></a>
    <nav class="side-nav">
      <p>WORKSPACE</p>
      <a href="${pageContext.request.contextPath}/"><i>⌂</i>Overview</a>
      <a href="${pageContext.request.contextPath}/conversations"><i>◫</i>Conversations</a>
      <a href="${pageContext.request.contextPath}/participants"><i>◎</i>Participants</a>
      <a href="${pageContext.request.contextPath}/messages/search"><i>⌕</i>Message Explorer</a>
      <p>TOOLS</p>
      <a class="active" href="${pageContext.request.contextPath}/import"><i>↥</i>Import Chat</a>
      <a href="${pageContext.request.contextPath}/analytics"><i>◌</i>Analytics</a>
    </nav>
  </aside>
  <main class="main">
    <header class="topbar"><button class="menu-button" data-sidebar-toggle aria-label="Toggle navigation">☰</button><div><div class="breadcrumb">Workspace / Data sources</div><h1>Import chat</h1></div><div class="avatar">CS</div></header>
    <div class="content">
      <section class="page-head"><div><p class="eyebrow">NEW DATA SOURCE</p><h2>Bring a conversation to life.</h2><p>Upload a CSV or paste messages directly into your workspace.</p></div></section>
      <% if(request.getAttribute("error")!=null){ %><div class="toast toast-error" data-toast><%=ViewUtil.escapeHtml((String)request.getAttribute("error"))%></div><% } %>
      <section class="import-method-grid">
        <form class="upload-panel" action="${pageContext.request.contextPath}/import" method="post" enctype="multipart/form-data">
          <div class="import-panel-heading"><span class="method-icon">↥</span><div><p class="eyebrow">CSV IMPORT</p><h3>Upload a chat export</h3></div></div>
          <label class="drop-zone" data-dropzone>
            <div class="drop-icon">↥</div><strong>Drop your CSV here</strong><span data-file-label>or click to browse · CSV up to 10 MB</span>
            <input type="file" name="chatFile" accept=".csv,text/csv" required>
          </label>
          <button class="button import-submit" type="submit">Import CSV messages</button>
        </form>
        <form class="upload-panel paste-panel" action="${pageContext.request.contextPath}/import" method="post">
          <div class="import-panel-heading"><span class="method-icon">⌕</span><div><p class="eyebrow">MANUAL PASTE</p><h3>Paste a conversation</h3></div></div>
          <div class="paste-fields">
            <label>Conversation title<input name="conversationTitle" required maxlength="255" placeholder="e.g. Weekend planning" value="<%=ViewUtil.escapeHtml(pastedTitle)%>"></label>
            <label>Source type<input name="sourceType" maxlength="100" placeholder="e.g. WhatsApp or Manual" value="<%=ViewUtil.escapeHtml(pastedSource)%>"></label>
          </div>
          <label class="paste-label">Messages
            <textarea name="pastedContent" required maxlength="500000" placeholder="Aisha: Are we still meeting today?&#10;Ravi: Yes, 6 PM works for me.&#10;&#10;Optional timestamps:&#10;[2026-11-02 14:30] Aisha: I will share the plan."><%=ViewUtil.escapeHtml(pastedContent)%></textarea>
          </label>
          <p class="paste-hint">Use one message per line as <code>Sender: message</code>. Timestamps are optional.</p>
          <button class="button import-submit" type="submit">Import pasted messages</button>
        </form>
      </section>
      <aside class="format-guide">
        <p class="eyebrow">CSV FORMAT</p><h3>A simple, predictable import.</h3>
        <div class="format-list"><div><b>Required columns</b><code>conversation_title,source_type,sender,sent_at,message</code></div><div><b>Timestamp</b><br>Use <code>2026-11-02 14:30</code> or ISO format.</div><div><b>Message text</b><br>Wrap CSV values containing commas in double quotes.</div><a class="action-link" download href="${pageContext.request.contextPath}/sample-chat.csv">↓ Download sample CSV</a></div>
      </aside>
    </div>
  </main>
</div>
<script src="${pageContext.request.contextPath}/assets/app.js"></script>
</body>
</html>
