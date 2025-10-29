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
