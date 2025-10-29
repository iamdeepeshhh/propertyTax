document.addEventListener('DOMContentLoaded', function () {
  const usePercent = document.getElementById('rvUsePercent');
  const percentInput = document.getElementById('rvPercent');
  if (!usePercent || !percentInput) return;

  const totalField = document.getElementById('prTotalRatableValueFl');

  const fieldIds = [
    'prResidentialFl',
    'prResidentialOpenPlotFl',
    'prCommercialFl',
    'prCommercialOpenPlotFl',
    'prEducationalFl',
    'prEducationAndLegalInstituteOpenPlotFl',
    'prIndustrialFl',
    'prIndustrialOpenPlotFl',
    'prGovernmentFl',
    'prGovernmentOpenPlotFl',
    'prReligiousFl',
    'prReligiousOpenPlotFl',
    'prElectricSubstationFl',
    'prMobileTowerFl'
  ];

  const getFields = () => fieldIds
    .map(id => document.getElementById(id))
    .filter(el => !!el);

  function captureBaseline() {
    getFields().forEach(el => {
      const val = parseFloat(el.value);
      el.dataset.original = isNaN(val) ? '0' : String(val);
    });
  }

  function computeTotal() {
    if (!totalField) return;
    const total = getFields()
      .map(el => parseFloat(el.value) || 0)
      .reduce((a, b) => a + b, 0);
    totalField.value = round2(total);
  }

  function applyPercent() {
    const p = parseFloat(percentInput.value);
    if (isNaN(p)) return;
    const factor = Math.max(0, Math.min(100, p)) / 100; // clamp 0..100
    let total = 0;
    getFields().forEach(el => {
      const base = parseFloat(el.dataset.original || el.value || '0') || 0;
      const reduced = round2(base * (1 - factor));
      el.value = Number.isFinite(reduced) ? reduced : 0;
      total += reduced;
    });
    if (totalField) totalField.value = round2(total);
  }

  function restoreBaseline() {
    let total = 0;
    getFields().forEach(el => {
      const base = parseFloat(el.dataset.original || el.value || '0') || 0;
      el.value = round2(base);
      total += base;
    });
    if (totalField) totalField.value = round2(total);
  }

  function round2(n) {
    return Math.round((n + Number.EPSILON) * 100) / 100;
  }

  usePercent.addEventListener('change', () => {
    if (usePercent.checked) {
      percentInput.disabled = false;
      captureBaseline();
      applyPercent();
    } else {
      percentInput.disabled = true;
      percentInput.value = '';
      restoreBaseline();
    }
  });

  percentInput.addEventListener('input', applyPercent);

  // If any field changes manually, refresh baseline when enabling again
  getFields().forEach(el => {
    const handler = () => {
      computeTotal();
      if (!usePercent.checked) {
        // keep original in sync with current values when not applying percent
        captureBaseline();
      }
    };
    el.addEventListener('input', handler);
    el.addEventListener('change', handler);
  });

  // Initialize baseline and total on load
  captureBaseline();
  computeTotal();
});
