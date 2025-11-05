// Extra Master Web behaviors: Report Tax Config save + helpers
(function () {
  function $(id) { return document.getElementById(id); }

  async function fetchJSON(url, options) {
    const res = await fetch(url, options);
    if (!res.ok) throw new Error(`HTTP ${res.status} @ ${url}`);
    return res.json().catch(() => ({}));
  }

  async function populateTemplateDropdownFromConfigs() {
    const sel = $('rtc-template');
    if (!sel) return;
    try {
      const all = await fetchJSON('/3g/reportTaxConfigs/all');
      const templates = Array.from(new Set((all || []).map(r => r.template).filter(Boolean))).sort();
      if (templates.length === 0) return; // keep existing hardcoded options if none in DB

      const current = sel.value;
      const opts = ['<option value="">Select Template</option>'];
      templates.forEach(t => opts.push(`<option value="${t}">${t}</option>`));
      sel.innerHTML = opts.join('');
      // Reapply current value if still present; otherwise pick first real option
      if (current && templates.includes(current)) sel.value = current;
      else sel.value = templates[0];
    } catch (e) {
      console.warn('Could not populate templates from configs:', e);
    }
  }

  async function populateTaxDropdown() {
    const select = $('rtc-taxselect');
    if (!select) return;
    try {
      const taxes = await fetchJSON('/3g/getAllConsolidatedTaxes');
      // Expect array of ConsolidatedTaxes_MasterDto with taxKeyL, taxNameVc
      const opts = ['<option value="">Select Tax</option>'];
      (taxes || []).forEach(t => {
        const key = t.taxKeyL ?? t.taxKey ?? t.id;
        const name = t.taxNameVc ?? t.englishname ?? t.localName ?? 'Tax';
        if (key != null) opts.push(`<option value="${key}">${name} (${key})</option>`);
      });
      select.innerHTML = opts.join('');
    } catch (e) {
      console.error('Failed to load consolidated taxes:', e);
    }
  }

  function collectReportTaxConfigForm() {
    const payload = {
      templateVc: $('rtc-template')?.value || 'CALCULATION_SHEET',
      sequenceI: parseInt($('rtc-sequence')?.value || '0', 10) || 0,
      taxKeyL: $('rtc-taxselect')?.value ? Number($('rtc-taxselect').value) : null,
      parentTaxKeyL: $('rtc-parentkey')?.value ? Number($('rtc-parentkey').value) : null,
      standardNameVc: $('rtc-standard')?.value || '',
      localNameVc: $('rtc-local')?.value || '',
      visibleB: $('rtc-visible')?.checked ?? true,
      showTotalBl: $('rtc-showtotal')?.checked ?? false
    };
    // Basic validation
    if (!payload.taxKeyL) throw new Error('Please select a tax');
    if (!payload.sequenceI) throw new Error('Sequence is required');
    if (!payload.standardNameVc) throw new Error('Standard name is required');
    return payload;
  }

  async function saveReportTaxConfig() {
    const btn = $('rtc-save-btn');
    if (!btn) return;
    btn.disabled = true;
    try {
      const item = collectReportTaxConfigForm();
      await fetch('/3g/reportTaxConfigs/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify([item]) // backend expects a list
      }).then(res => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
      });
      // Refresh table listing
      if (typeof fetchAndPopulateTable === 'function') {
        fetchAndPopulateTable('/3g/reportTaxConfigs/all', 'rtc-table-body', ['template', 'sequence', 'englishname', 'marathiname', 'visible', 'showTotal']);
      }
      // Light feedback
      btn.textContent = 'Saved';
      setTimeout(() => (btn.textContent = 'Save', btn.disabled = false), 1000);
    } catch (e) {
      console.error('Save ReportTaxConfig failed:', e);
      btn.textContent = 'Error';
      setTimeout(() => (btn.textContent = 'Save', btn.disabled = false), 1200);
      alert(e.message || 'Failed to save');
    }
  }

  function wireDefaults() {
    const taxSel = $('rtc-taxselect');
    if (taxSel) {
      taxSel.addEventListener('change', () => {
        // On tax select, if names empty, hint from option text
        const opt = taxSel.options[taxSel.selectedIndex];
        const name = opt ? opt.text.replace(/\s*\(\d+\)$/, '') : '';
        if ($('rtc-standard') && !$('rtc-standard').value) $('rtc-standard').value = name;
        if ($('rtc-local') && !$('rtc-local').value) $('rtc-local').value = name;
      });
    }
    const btn = $('rtc-save-btn');
    if (btn) btn.addEventListener('click', saveReportTaxConfig);
  }

  document.addEventListener('DOMContentLoaded', () => {
    populateTemplateDropdownFromConfigs();
    populateTaxDropdown();
    wireDefaults();
    wireArrearsYearListener();
    // expose total calc for inline onclick and also recalc on edits
    window.calculateArrearsTotal = calculateArrearsTotal;
    autoWireArrearsTotal();
    setupCompareObjectionList();
  });
})();

// ==========================
// Arrears helpers (client)
// ==========================
function wireArrearsYearListener() {
  const yearSel = document.getElementById('financialYear');
  const npInput = document.getElementById('newPropertyNo');
  if (!yearSel || !npInput) return;
  yearSel.addEventListener('change', async function () {
    const fy = this.value;
    const np = npInput.value;
    if (!fy || !np) return;
    try {
      const res = await fetch(`/3g/getPropertyArrearsByYear?newPropertyNo=${encodeURIComponent(np)}&financialYear=${encodeURIComponent(fy)}`);
      if (res.status === 404) {
        clearArrearsTaxFields();
        return;
      }
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const dto = await res.json();
      populateArrearsTaxForm(dto);
      calculateArrearsTotal();
    } catch (e) {
      console.error('Failed to load arrears by year:', e);
      clearArrearsTaxFields();
    }
  });
}

function populateArrearsTaxForm(dto) {
  if (!dto) return;
  Object.entries(dto).forEach(([k, v]) => {
    const el = document.getElementById(k);
    if (el) {
      if (el.type === 'number') el.value = v == null || v === '' ? 0 : v;
      else el.value = v == null ? '' : v;
    }
  });
}

function clearArrearsTaxFields() {
  const form = document.getElementById('arrearsTaxForm');
  if (!form) return;
  Array.from(form.querySelectorAll('input[type="number"]')).forEach(inp => {
    if (inp.id !== 'totalTax') inp.value = 0;
  });
}

function calculateArrearsTotal() {
  const form = document.getElementById('arrearsTaxForm');
  const totalEl = document.getElementById('totalTax');
  if (!form || !totalEl) return;
  let sum = 0;
  form.querySelectorAll('input[type="number"]').forEach(inp => {
    if (inp.id === 'totalTax') return;
    const n = parseFloat(inp.value);
    if (!isNaN(n)) sum += n;
  });
  totalEl.value = Math.round((sum + Number.EPSILON) * 100) / 100;
}

function autoWireArrearsTotal() {
  const form = document.getElementById('arrearsTaxForm');
  if (!form) return;
  const handler = () => calculateArrearsTotal();
  form.querySelectorAll('input[type="number"]').forEach(inp => {
    inp.addEventListener('input', handler);
    inp.addEventListener('change', handler);
  });
}

// ---------------- Before/After Compare (MasterWeb Part 2) -----------------
document.addEventListener('DOMContentLoaded', function() {
  const btn = document.getElementById('cba-load');
  if (!btn) return;
  btn.addEventListener('click', async function() {
    const input = document.getElementById('cba-newprop');
    const propNo = (input && input.value || '').trim();
    if (!propNo) { alert('Enter Final or New Property No'); return; }
    try {
      const res = await fetch(`/3g/afterHearing/compareProperty?finalPropertyNo=${encodeURIComponent(propNo)}`);
      if (!res.ok) throw new Error('Failed to load compare data');
      const data = await res.json();
      renderCompare(data);
    } catch (e) {
      console.error(e);
      alert('Unable to load compare data');
    }
  });
});

function setupCompareObjectionList() {
  const section = document.getElementById('compareBeforeAfter');
  if (!section) return;
  if (!document.getElementById('cba-obj-tbody')) {
    const card = document.createElement('div');
    card.className = 'card p-3 mb-3';
    card.innerHTML = `
      <div class="d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Objections Taken</h5>
        <button type="button" class="btn btn-sm btn-outline-secondary" id="cba-refresh-obj">Refresh</button>
      </div>
      <div class="table-responsive mt-2">
        <table class="table table-sm table-bordered mb-0">
          <thead class="thead-light">
            <tr>
              <th>Ward</th>
              <th>Final No</th>
              <th>New No</th>
              <th>Owner</th>
              <th>Status</th>
              <th>Change</th>
              <th>Objection Date</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody id="cba-obj-tbody"></tbody>
        </table>
      </div>`;
    // insert card at the top of section
    const firstChild = section.firstElementChild;
    section.insertBefore(card, firstChild);
  }

  const refreshBtn = document.getElementById('cba-refresh-obj');
  if (refreshBtn) refreshBtn.addEventListener('click', loadObjectionTakenList);
  // initial load
  loadObjectionTakenList();

  // delegate click for view changes
  section.addEventListener('click', function (ev) {
    const btn = ev.target.closest('.cba-view-change');
    if (!btn) return;
    const finalNo = btn.getAttribute('data-final');
    const input = document.getElementById('cba-newprop');
    if (input) input.value = finalNo || '';
    const trigger = document.getElementById('cba-load');
    if (trigger) trigger.click();
  });
}

async function loadObjectionTakenList() {
  const body = document.getElementById('cba-obj-tbody');
  if (!body) return;
  body.innerHTML = '<tr><td colspan="8">Loading...</td></tr>';
  try {
    const res = await fetch('/3g/getObjectionTakenProperties');
    if (res.status === 204) { body.innerHTML = '<tr><td colspan="8">No records</td></tr>'; return; }
    if (!res.ok) throw new Error('HTTP ' + res.status);
    const list = await res.json();
    if (!Array.isArray(list) || list.length === 0) {
      body.innerHTML = '<tr><td colspan="8">No records</td></tr>';
      return;
    }
    body.innerHTML = list.map((r, idx) => {
      const ward = r.wardNo != null ? r.wardNo : '';
      const fno = r.finalPropertyNo || '';
      const nno = r.newPropertyNo || '';
      const owner = r.ownerName || '';
      const status = r.hearingStatus || '';
      const change = r.changedValue || '';
      const date = r.objectionDate || '';
      return `<tr>
        <td>${ward}</td>
        <td>${fno}</td>
        <td>${nno}</td>
        <td>${owner}</td>
        <td>${status}</td>
        <td>${change}</td>
        <td>${date}</td>
        <td><button type="button" class="btn btn-sm btn-primary cba-view-change" data-final="${fno}">View Changes</button></td>
      </tr>`;
    }).join('');
  } catch (e) {
    console.error('Failed to load objections list:', e);
    body.innerHTML = '<tr><td colspan="8">Failed to load</td></tr>';
  }
}

function renderCompare(data){
  const before = data && data.before || {};
  const after = data && data.after || {};

  const bpd = before.propertyDetails || {};
  const apd = after.propertyDetails || {};
  const bUnits = before.unitDetails || [];
  const aUnits = after.unitDetails || [];

  const bSummary = [
    `Owner: ${bpd.pdOwnernameVc || ''}`,
    `Address: ${bpd.pdPropertyaddressVc || ''}`,
    `Ward: ${bpd.pdWardI || ''}`,
    `Final No: ${bpd.pdFinalpropnoVc || ''}`,
    `Units: ${bUnits.length}`
  ].join(' | ');

  const aSummary = [
    `Owner: ${apd.pdOwnernameVc || ''}`,
    `Address: ${apd.pdPropertyaddressVc || ''}`,
    `Ward: ${apd.pdWardI || ''}`,
    `Final No: ${apd.pdFinalpropnoVc || ''}`,
    `Units: ${aUnits.length}`
  ].join(' | ');

  const bSumEl = document.getElementById('cba-before-summary');
  const aSumEl = document.getElementById('cba-after-summary');
  const bJsonEl = document.getElementById('cba-before-json');
  const aJsonEl = document.getElementById('cba-after-json');
  if (bSumEl) bSumEl.textContent = bSummary;
  if (aSumEl) aSumEl.textContent = aSummary;
  if (bJsonEl) bJsonEl.textContent = JSON.stringify(before, null, 2);
  if (aJsonEl) aJsonEl.textContent = JSON.stringify(after, null, 2);
}

// =======================
// Hearing Scheduler (Master → Notices) – part2
// =======================
function _sel(id){ return document.getElementById(id); }

function getSchedulerForm() {
  const wardNo = Number(_sel('sch-ward')?.value || 0) || null;
  const fromFinal = _sel('sch-from')?.value?.trim() || null;
  const toFinal = _sel('sch-to')?.value?.trim() || null;
  const hearingDate = _sel('sch-date')?.value || null;
  const startTime = _sel('sch-time')?.value || null;
  // expectedCount will be attached at schedule time from last preview
  return { wardNo, fromFinal, toFinal, hearingDate, startTime };
}

function renderSchedulerRows(list) {
  const body = _sel('schedulerResultBody');
  if (!body) return;
  body.innerHTML = '';
  if (!Array.isArray(list) || list.length === 0) {
    body.innerHTML = '<tr><td colspan="5">No records</td></tr>';
    return;
  }
  body.innerHTML = list.map((r, idx) => `
    <tr>
      <td>${idx + 1}</td>
      <td>${r.finalPropertyNo || ''}</td>

      <td>${r.ownerName || ''}</td>
      <td>${r.hearingDate || ''}</td>
      <td>${r.hearingTime || ''}</td>
    </tr>
  `).join('');
}

// Extra Master Web behaviors: Report Tax Config save + helpers
(function () {
  function $(id) { return document.getElementById(id); }

  async function fetchJSON(url, options) {
    const res = await fetch(url, options);
    if (!res.ok) throw new Error(`HTTP ${res.status} @ ${url}`);
    return res.json().catch(() => ({}));
  }

  async function populateTemplateDropdownFromConfigs() {
    const sel = $('rtc-template');
    if (!sel) return;
    try {
      const all = await fetchJSON('/3g/reportTaxConfigs/all');
      const templates = Array.from(new Set((all || []).map(r => r.template).filter(Boolean))).sort();
      if (templates.length === 0) return; // keep existing hardcoded options if none in DB

      const current = sel.value;
      const opts = ['<option value="">Select Template</option>'];
      templates.forEach(t => opts.push(`<option value="${t}">${t}</option>`));
      sel.innerHTML = opts.join('');
      // Reapply current value if still present; otherwise pick first real option
      if (current && templates.includes(current)) sel.value = current;
      else sel.value = templates[0];
    } catch (e) {
      console.warn('Could not populate templates from configs:', e);
    }
  }

  async function populateTaxDropdown() {
    const select = $('rtc-taxselect');
    if (!select) return;
    try {
      const taxes = await fetchJSON('/3g/getAllConsolidatedTaxes');
      // Expect array of ConsolidatedTaxes_MasterDto with taxKeyL, taxNameVc
      const opts = ['<option value="">Select Tax</option>'];
      (taxes || []).forEach(t => {
        const key = t.taxKeyL ?? t.taxKey ?? t.id;
        const name = t.taxNameVc ?? t.englishname ?? t.localName ?? 'Tax';
        if (key != null) opts.push(`<option value="${key}">${name} (${key})</option>`);
      });
      select.innerHTML = opts.join('');
    } catch (e) {
      console.error('Failed to load consolidated taxes:', e);
    }
  }

  function collectReportTaxConfigForm() {
    const payload = {
      templateVc: $('rtc-template')?.value || 'CALCULATION_SHEET',
      sequenceI: parseInt($('rtc-sequence')?.value || '0', 10) || 0,
      taxKeyL: $('rtc-taxselect')?.value ? Number($('rtc-taxselect').value) : null,
      parentTaxKeyL: $('rtc-parentkey')?.value ? Number($('rtc-parentkey').value) : null,
      standardNameVc: $('rtc-standard')?.value || '',
      localNameVc: $('rtc-local')?.value || '',
      visibleB: $('rtc-visible')?.checked ?? true,
      showTotalBl: $('rtc-showtotal')?.checked ?? false
    };
    // Basic validation
    if (!payload.taxKeyL) throw new Error('Please select a tax');
    if (!payload.sequenceI) throw new Error('Sequence is required');
    if (!payload.standardNameVc) throw new Error('Standard name is required');
    return payload;
  }

  async function saveReportTaxConfig() {
    const btn = $('rtc-save-btn');
    if (!btn) return;
    btn.disabled = true;
    try {
      const item = collectReportTaxConfigForm();
      await fetch('/3g/reportTaxConfigs/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify([item]) // backend expects a list
      }).then(res => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
      });
      // Refresh table listing
      if (typeof fetchAndPopulateTable === 'function') {
        fetchAndPopulateTable('/3g/reportTaxConfigs/all', 'rtc-table-body', ['template', 'sequence', 'englishname', 'marathiname', 'visible', 'showTotal']);
      }
      // Light feedback
      btn.textContent = 'Saved';
      setTimeout(() => (btn.textContent = 'Save', btn.disabled = false), 1000);
    } catch (e) {
      console.error('Save ReportTaxConfig failed:', e);
      btn.textContent = 'Error';
      setTimeout(() => (btn.textContent = 'Save', btn.disabled = false), 1200);
      alert(e.message || 'Failed to save');
    }
  }

  function wireDefaults() {
    const taxSel = $('rtc-taxselect');
    if (taxSel) {
      taxSel.addEventListener('change', () => {
        // On tax select, if names empty, hint from option text
        const opt = taxSel.options[taxSel.selectedIndex];
        const name = opt ? opt.text.replace(/\s*\(\d+\)$/, '') : '';
        if ($('rtc-standard') && !$('rtc-standard').value) $('rtc-standard').value = name;
        if ($('rtc-local') && !$('rtc-local').value) $('rtc-local').value = name;
      });
    }
    const btn = $('rtc-save-btn');
    if (btn) btn.addEventListener('click', saveReportTaxConfig);
  }

  document.addEventListener('DOMContentLoaded', () => {
    populateTemplateDropdownFromConfigs();
    populateTaxDropdown();
    wireDefaults();
    wireArrearsYearListener();
    // expose total calc for inline onclick and also recalc on edits
    window.calculateArrearsTotal = calculateArrearsTotal;
    autoWireArrearsTotal();
    setupCompareObjectionList();
  });
})();

// ==========================
// Arrears helpers (client)
// ==========================
function wireArrearsYearListener() {
  const yearSel = document.getElementById('financialYear');
  const npInput = document.getElementById('newPropertyNo');
  if (!yearSel || !npInput) return;
  yearSel.addEventListener('change', async function () {
    const fy = this.value;
    const np = npInput.value;
    if (!fy || !np) return;
    try {
      const res = await fetch(`/3g/getPropertyArrearsByYear?newPropertyNo=${encodeURIComponent(np)}&financialYear=${encodeURIComponent(fy)}`);
      if (res.status === 404) {
        clearArrearsTaxFields();
        return;
      }
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const dto = await res.json();
      populateArrearsTaxForm(dto);
      calculateArrearsTotal();
    } catch (e) {
      console.error('Failed to load arrears by year:', e);
      clearArrearsTaxFields();
    }
  });
}

function populateArrearsTaxForm(dto) {
  if (!dto) return;
  Object.entries(dto).forEach(([k, v]) => {
    const el = document.getElementById(k);
    if (el) {
      if (el.type === 'number') el.value = v == null || v === '' ? 0 : v;
      else el.value = v == null ? '' : v;
    }
  });
}

function clearArrearsTaxFields() {
  const form = document.getElementById('arrearsTaxForm');
  if (!form) return;
  Array.from(form.querySelectorAll('input[type="number"]')).forEach(inp => {
    if (inp.id !== 'totalTax') inp.value = 0;
  });
}

function calculateArrearsTotal() {
  const form = document.getElementById('arrearsTaxForm');
  const totalEl = document.getElementById('totalTax');
  if (!form || !totalEl) return;
  let sum = 0;
  form.querySelectorAll('input[type="number"]').forEach(inp => {
    if (inp.id === 'totalTax') return;
    const n = parseFloat(inp.value);
    if (!isNaN(n)) sum += n;
  });
  totalEl.value = Math.round((sum + Number.EPSILON) * 100) / 100;
}

function autoWireArrearsTotal() {
  const form = document.getElementById('arrearsTaxForm');
  if (!form) return;
  const handler = () => calculateArrearsTotal();
  form.querySelectorAll('input[type="number"]').forEach(inp => {
    inp.addEventListener('input', handler);
    inp.addEventListener('change', handler);
  });
}

// ---------------- Before/After Compare (MasterWeb Part 2) -----------------
document.addEventListener('DOMContentLoaded', function() {
  const btn = document.getElementById('cba-load');
  if (!btn) return;
  btn.addEventListener('click', async function() {
    const input = document.getElementById('cba-newprop');
    const propNo = (input && input.value || '').trim();
    if (!propNo) { alert('Enter Final or New Property No'); return; }
    try {
      const res = await fetch(`/3g/afterHearing/compareProperty?finalPropertyNo=${encodeURIComponent(propNo)}`);
      if (!res.ok) throw new Error('Failed to load compare data');
      const data = await res.json();
      renderCompare(data);
    } catch (e) {
      console.error(e);
      alert('Unable to load compare data');
    }
  });
});

function setupCompareObjectionList() {
  const section = document.getElementById('compareBeforeAfter');
  if (!section) return;
  if (!document.getElementById('cba-obj-tbody')) {
    const card = document.createElement('div');
    card.className = 'card p-3 mb-3';
    card.innerHTML = `
      <div class="d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Objections Taken</h5>
        <button type="button" class="btn btn-sm btn-outline-secondary" id="cba-refresh-obj">Refresh</button>
      </div>
      <div class="table-responsive mt-2">
        <table class="table table-sm table-bordered mb-0">
          <thead class="thead-light">
            <tr>
              <th>Ward</th>
              <th>Final No</th>
              <th>New No</th>
              <th>Owner</th>
              <th>Status</th>
              <th>Change</th>
              <th>Objection Date</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody id="cba-obj-tbody"></tbody>
        </table>
      </div>`;
    // insert card at the top of section
    const firstChild = section.firstElementChild;
    section.insertBefore(card, firstChild);
  }

  const refreshBtn = document.getElementById('cba-refresh-obj');
  if (refreshBtn) refreshBtn.addEventListener('click', loadObjectionTakenList);
  // initial load
  loadObjectionTakenList();

  // delegate click for view changes
  section.addEventListener('click', function (ev) {
    const btn = ev.target.closest('.cba-view-change');
    if (!btn) return;
    const finalNo = btn.getAttribute('data-final');
    const input = document.getElementById('cba-newprop');
    if (input) input.value = finalNo || '';
    const trigger = document.getElementById('cba-load');
    if (trigger) trigger.click();
  });
}

async function loadObjectionTakenList() {
  const body = document.getElementById('cba-obj-tbody');
  if (!body) return;
  body.innerHTML = '<tr><td colspan="8">Loading...</td></tr>';
  try {
    const res = await fetch('/3g/getObjectionTakenProperties');
    if (res.status === 204) { body.innerHTML = '<tr><td colspan="8">No records</td></tr>'; return; }
    if (!res.ok) throw new Error('HTTP ' + res.status);
    const list = await res.json();
    if (!Array.isArray(list) || list.length === 0) {
      body.innerHTML = '<tr><td colspan="8">No records</td></tr>';
      return;
    }
    body.innerHTML = list.map((r, idx) => {
      const ward = r.wardNo != null ? r.wardNo : '';
      const fno = r.finalPropertyNo || '';
      const nno = r.newPropertyNo || '';
      const owner = r.ownerName || '';
      const status = r.hearingStatus || '';
      const change = r.changedValue || '';
      const date = r.objectionDate || '';
      return `<tr>
        <td>${ward}</td>
        <td>${fno}</td>
        <td>${nno}</td>
        <td>${owner}</td>
        <td>${status}</td>
        <td>${change}</td>
        <td>${date}</td>
        <td><button type="button" class="btn btn-sm btn-primary cba-view-change" data-final="${fno}">View Changes</button></td>
      </tr>`;
    }).join('');
  } catch (e) {
    console.error('Failed to load objections list:', e);
    body.innerHTML = '<tr><td colspan="8">Failed to load</td></tr>';
  }
}

function renderCompare(data){
  const before = data && data.before || {};
  const after = data && data.after || {};

  const bpd = before.propertyDetails || {};
  const apd = after.propertyDetails || {};
  const bUnits = before.unitDetails || [];
  const aUnits = after.unitDetails || [];

  const bSummary = [
    `Owner: ${bpd.pdOwnernameVc || ''}`,
    `Address: ${bpd.pdPropertyaddressVc || ''}`,
    `Ward: ${bpd.pdWardI || ''}`,
    `Final No: ${bpd.pdFinalpropnoVc || ''}`,
    `Units: ${bUnits.length}`
  ].join(' | ');

  const aSummary = [
    `Owner: ${apd.pdOwnernameVc || ''}`,
    `Address: ${apd.pdPropertyaddressVc || ''}`,
    `Ward: ${apd.pdWardI || ''}`,
    `Final No: ${apd.pdFinalpropnoVc || ''}`,
    `Units: ${aUnits.length}`
  ].join(' | ');

  const bSumEl = document.getElementById('cba-before-summary');
  const aSumEl = document.getElementById('cba-after-summary');
  const bJsonEl = document.getElementById('cba-before-json');
  const aJsonEl = document.getElementById('cba-after-json');
  if (bSumEl) bSumEl.textContent = bSummary;
  if (aSumEl) aSumEl.textContent = aSummary;
  if (bJsonEl) bJsonEl.textContent = JSON.stringify(before, null, 2);
  if (aJsonEl) aJsonEl.textContent = JSON.stringify(after, null, 2);
}

// =======================
// Hearing Scheduler (Master → Notices) – part2
// =======================
function _sel(id){ return document.getElementById(id); }

function getSchedulerForm() {
  const wardNo = Number(_sel('sch-ward')?.value || 0) || null;
  const fromFinal = _sel('sch-from')?.value?.trim() || null;
  const toFinal = _sel('sch-to')?.value?.trim() || null;
  const hearingDate = _sel('sch-date')?.value || null;
  const startTime = _sel('sch-time')?.value || null;
  const slotMinutes = Number(_sel('sch-slot')?.value || 10);
  const overwriteExisting = !!_sel('sch-overwrite')?.checked;
  return { wardNo, fromFinal, toFinal, hearingDate, startTime, slotMinutes, overwriteExisting };
}

function renderSchedulerRows(list) {
  const body = _sel('schedulerResultBody');
  if (!body) return;
  body.innerHTML = '';
  if (!Array.isArray(list) || list.length === 0) {
    body.innerHTML = '<tr><td colspan="5">No records</td></tr>';
    return;
  }
  body.innerHTML = list.map((r, idx) => `
    <tr>
      <td>${idx + 1}</td>
      <td>${r.finalPropertyNo || ''}</td>
      <td>${r.ownerName || ''}</td>
      <td>${r.hearingDate || ''}</td>
      <td>${r.hearingTime || ''}</td>
    </tr>
  `).join('');
}

window.previewHearingSchedule = async function previewHearingSchedule() {
  try {
    const f = getSchedulerForm();
    if (!f.wardNo) { alert('Ward is required'); return; }
    const params = new URLSearchParams();
    params.set('wardNo', f.wardNo);
    if (f.fromFinal) params.set('fromFinal', f.fromFinal);
    if (f.toFinal) params.set('toFinal', f.toFinal);
    const res = await fetch(`/3g/objections/scheduler/preview?${params.toString()}`);
    if (res.status === 204) { renderSchedulerRows([]); return; }
    if (!res.ok) throw new Error('HTTP ' + res.status);
    const data = await res.json();
    renderSchedulerRows(data);
    // store preview count for optimistic concurrency on schedule
    window.__schedulerPreviewCount = Array.isArray(data) ? data.length : null;
  } catch (e) {
    console.error('Preview failed', e);
    alert('Preview failed');
  }
}

window.runHearingSchedule = async function runHearingSchedule() {
  try {
    const f = getSchedulerForm();
    if (!f.wardNo || !f.hearingDate || !f.startTime) {
      alert('Ward, Hearing Date and Start Time are required');
      return;
    }
    // Attach expectedCount if we have a recent preview
    if (typeof window.__schedulerPreviewCount === 'number') {
      f.expectedCount = window.__schedulerPreviewCount;
    }
    const res = await fetch('/3g/objections/scheduler/schedule', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(f)
    });
    if (res.status === 204) { renderSchedulerRows([]); return; }
    if (!res.ok) throw new Error('HTTP ' + res.status);
    const data = await res.json();
    renderSchedulerRows(data);
    alert(`Scheduled ${Array.isArray(data) ? data.length : 0} records.`);
  } catch (e) {
    console.error('Schedule failed', e);
    alert('Schedule failed');
  }
}
