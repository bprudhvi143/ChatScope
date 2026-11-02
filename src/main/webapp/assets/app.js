/*
 * Each JSP shares this small client bootstrap.  Append a versioned stylesheet
 * so pages that were previously served with the old, unversioned app.css
 * immediately receive the current design system as well.
 */
(() => {
  const themeHref = `${window.location.pathname.split('/').slice(0, 2).join('/')}/assets/app.css?v=3`;
  if (!document.querySelector('link[data-chatscope-theme]')) {
    const theme = document.createElement('link');
    theme.rel = 'stylesheet';
    theme.href = themeHref;
    theme.dataset.chatscopeTheme = 'true';
    document.head.append(theme);
  }

  // Old views may have a cached chart script or an inline-data policy that
  // prevents the original renderer from starting. Load the versioned renderer
  // whenever a dashboard chart canvas is present.
  if (document.querySelector('canvas[id$="Chart"]') && !document.querySelector('script[data-chatscope-charts]')) {
    const charts = document.createElement('script');
    charts.src = `${window.location.pathname.split('/').slice(0, 2).join('/')}/assets/charts.js?v=3`;
    charts.dataset.chatscopeCharts = 'true';
    document.head.append(charts);
  }
})();

document.addEventListener('DOMContentLoaded', () => {
  const toggle = document.querySelector('[data-sidebar-toggle]');
  const shell = document.querySelector('.app-shell');
  if (toggle && shell) toggle.addEventListener('click', () => shell.classList.toggle('sidebar-open'));

  document.querySelectorAll('[data-dropzone]').forEach(zone => {
    const input = zone.querySelector('input[type=file]');
    const label = zone.querySelector('[data-file-label]');
    const show = () => { if (input.files[0]) { label.textContent = input.files[0].name; zone.classList.add('has-file'); } };
    input.addEventListener('change', show);
    ['dragenter','dragover'].forEach(event => zone.addEventListener(event, e => { e.preventDefault(); zone.classList.add('dragging'); }));
    ['dragleave','drop'].forEach(event => zone.addEventListener(event, e => { e.preventDefault(); zone.classList.remove('dragging'); }));
    zone.addEventListener('drop', e => { if (e.dataTransfer.files.length) { input.files = e.dataTransfer.files; show(); } });
  });
  document.querySelectorAll('[data-toast]').forEach(toast => setTimeout(() => toast.classList.add('toast-hide'), 5000));
});
