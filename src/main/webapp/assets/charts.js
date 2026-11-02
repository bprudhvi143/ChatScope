(() => {
  const palette = ['#635bff', '#7c6cff', '#46a5ff', '#9e80ff', '#77d4c3', '#ff9e7a'];

  const canvas = (id) => document.getElementById(id);
  const empty = (id, visible) => document.querySelector(`[data-empty="${id}"]`)?.classList.toggle('show', visible);

  const context = (element) => {
    const ratio = window.devicePixelRatio || 1;
    const width = Math.max(element.clientWidth, 240);
    const height = Math.max(element.clientHeight, 180);
    element.width = Math.round(width * ratio);
    element.height = Math.round(height * ratio);
    const ctx = element.getContext('2d');
    ctx.setTransform(ratio, 0, 0, ratio, 0, 0);
    return { ctx, width, height };
  };

  const text = (ctx, value, x, y, align = 'left') => {
    ctx.fillStyle = '#8a90a3';
    ctx.font = '10px Inter, system-ui, sans-serif';
    ctx.textAlign = align;
    ctx.fillText(String(value), x, y);
  };

  const drawLine = (element, values) => {
    if (!element || !values.length) return;
    const { ctx, width, height } = context(element);
    const pad = { left: 36, right: 14, top: 16, bottom: 29 };
    const graphWidth = width - pad.left - pad.right;
    const graphHeight = height - pad.top - pad.bottom;
    const max = Math.max(...values.map(item => item.value), 1);
    ctx.strokeStyle = '#ececf4';
    ctx.lineWidth = 1;
    for (let row = 0; row < 4; row++) {
      const y = pad.top + (graphHeight * row / 3);
      ctx.beginPath(); ctx.moveTo(pad.left, y); ctx.lineTo(width - pad.right, y); ctx.stroke();
      text(ctx, Math.round(max - (max * row / 3)), pad.left - 7, y + 3, 'right');
    }
    const points = values.map((item, index) => ({
      x: pad.left + (values.length === 1 ? graphWidth / 2 : graphWidth * index / (values.length - 1)),
      y: pad.top + graphHeight - (item.value / max * graphHeight)
    }));
    const gradient = ctx.createLinearGradient(0, pad.top, 0, pad.top + graphHeight);
    gradient.addColorStop(0, 'rgba(99,91,255,.26)'); gradient.addColorStop(1, 'rgba(99,91,255,0)');
    ctx.beginPath(); ctx.moveTo(points[0].x, pad.top + graphHeight);
    points.forEach(point => ctx.lineTo(point.x, point.y));
    ctx.lineTo(points.at(-1).x, pad.top + graphHeight); ctx.closePath(); ctx.fillStyle = gradient; ctx.fill();
    ctx.beginPath(); points.forEach((point, index) => index ? ctx.lineTo(point.x, point.y) : ctx.moveTo(point.x, point.y));
    ctx.strokeStyle = '#635bff'; ctx.lineWidth = 2.5; ctx.stroke();
    ctx.fillStyle = '#635bff'; points.forEach(point => { ctx.beginPath(); ctx.arc(point.x, point.y, 2.5, 0, Math.PI * 2); ctx.fill(); });
    [0, Math.floor((values.length - 1) / 2), values.length - 1].filter((value, index, all) => all.indexOf(value) === index)
      .forEach(index => text(ctx, values[index].label.slice(5), points[index].x, height - 8, 'center'));
  };

  const drawBars = (element, values, horizontal = false) => {
    if (!element || !values.length) return;
    const { ctx, width, height } = context(element);
    const pad = horizontal ? { left: 88, right: 22, top: 12, bottom: 10 } : { left: 32, right: 12, top: 12, bottom: 27 };
    const max = Math.max(...values.map(item => item.value), 1);
    const areaWidth = width - pad.left - pad.right;
    const areaHeight = height - pad.top - pad.bottom;
    if (horizontal) {
      const row = areaHeight / values.length;
      values.forEach((item, index) => {
        const y = pad.top + index * row + 4;
        const barWidth = Math.max(3, item.value / max * areaWidth);
        ctx.fillStyle = palette[index % palette.length];
        ctx.beginPath(); ctx.roundRect(pad.left, y, barWidth, Math.max(8, row - 9), 5); ctx.fill();
        text(ctx, item.label.length > 13 ? `${item.label.slice(0, 12)}…` : item.label, pad.left - 7, y + row / 2, 'right');
        text(ctx, item.value, Math.min(width - 3, pad.left + barWidth + 5), y + row / 2);
      });
      return;
    }
    const slot = areaWidth / values.length;
    values.forEach((item, index) => {
      const barHeight = Math.max(2, item.value / max * areaHeight);
      const barWidth = Math.max(3, slot * .62);
      const x = pad.left + index * slot + (slot - barWidth) / 2;
      const y = pad.top + areaHeight - barHeight;
      ctx.fillStyle = '#46a5ff'; ctx.beginPath(); ctx.roundRect(x, y, barWidth, barHeight, 4); ctx.fill();
      if (values.length <= 12 || index % Math.ceil(values.length / 6) === 0) text(ctx, item.label, x + barWidth / 2, height - 8, 'center');
    });
  };

  const drawDonut = (element, values) => {
    if (!element || !values.length) return;
    const { ctx, width, height } = context(element);
    const total = values.reduce((sum, item) => sum + item.value, 0) || 1;
    const centerX = width / 2, centerY = height / 2 - 7, radius = Math.min(width, height) * .29;
    let angle = -Math.PI / 2;
    values.forEach((item, index) => {
      const next = angle + item.value / total * Math.PI * 2;
      ctx.beginPath(); ctx.strokeStyle = palette[index % palette.length]; ctx.lineWidth = Math.max(18, radius * .36);
      ctx.arc(centerX, centerY, radius, angle + .025, next - .025); ctx.stroke(); angle = next;
    });
    ctx.fillStyle = '#22263a'; ctx.font = '700 19px Inter, system-ui, sans-serif'; ctx.textAlign = 'center'; ctx.fillText(total.toLocaleString(), centerX, centerY + 5);
    text(ctx, 'messages', centerX, centerY + 21, 'center');
    values.slice(0, 2).forEach((item, index) => {
      const x = width * (index ? .75 : .25);
      const y = height - 14;
      const label = item.label.length > 18 ? `${item.label.slice(0, 17)}…` : item.label;
      ctx.fillStyle = palette[index]; ctx.fillRect(x - 50, y - 7, 7, 7);
      text(ctx, label, x - 39, y);
    });
  };

  const render = (data) => {
    drawLine(canvas('trendChart'), data.daily || []);
    drawBars(canvas('hourChart'), data.hourly || []);
    drawBars(canvas('peopleChart'), data.people || [], true);
    drawDonut(canvas('conversationChart'), data.conversations || []);
    empty('trendChart', !(data.daily || []).length);
    empty('hourChart', !(data.hourly || []).length);
    empty('peopleChart', !(data.people || []).length);
    empty('conversationChart', !(data.conversations || []).length);
  };

  const contextPath = window.location.pathname.split('/').slice(0, 2).join('/');
  fetch(`${contextPath}/api/analytics`, { credentials: 'same-origin' })
    .then(response => response.ok ? response.json() : Promise.reject(response))
    .then(render)
    .catch(() => render({ daily: [], hourly: [], people: [], conversations: [] }));
})();
