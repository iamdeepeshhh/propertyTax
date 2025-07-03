document.addEventListener('DOMContentLoaded', function () {
  // Set council name
  fetch('/3g/getCouncilDetails')
    .then(res => res.json())
    .then(data => {
      const name = data[0]?.standardName || '3G Associates';
      document.getElementById('councilName').textContent = name;
    })
    .catch(() => {
      document.getElementById('councilName').textContent = '3G Associates';
    });

  // Fetch ward-wise count
  fetch('/3gSurvey/getPropertiesCount')
    .then(res => res.json())
    .then(data => {
      const tbody = document.getElementById('wardWiseTableBody');
      tbody.innerHTML = '';
      let totalProperties = 0;

      for (const [ward, count] of Object.entries(data)) {
        const numericCount = parseInt(count);
        totalProperties += numericCount;

        const row = document.createElement('tr');
        row.innerHTML = `<td>${ward}</td><td>${numericCount}</td>`;
        tbody.appendChild(row);
      }

    })
    .catch(() => {
      document.getElementById('wardWiseTableBody').innerHTML =
        '<tr><td colspan="2">Error loading data</td></tr>';
    });
});
