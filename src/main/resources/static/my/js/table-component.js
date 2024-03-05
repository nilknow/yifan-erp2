class TableComponent {
    constructor(containerId, headerText, data) {
        this.container = document.getElementById(containerId);
        this.headerText = headerText;
        this.data = data;
        this.render();
    }

    render() {
        const table = document.createElement('table');
        const thead = document.createElement('thead');
        const tbody = document.createElement('tbody');
        const headerRow = document.createElement('tr');
        const headerCell = document.createElement('th');

        headerCell.textContent = this.headerText;
        headerRow.appendChild(headerCell);
        thead.appendChild(headerRow);
        table.appendChild(thead);
        table.appendChild(tbody);

        this.data.forEach(item => {
            const row = document.createElement('tr');
            const cell = document.createElement('td');
            cell.textContent = item.name;
            let category = document.createElement("td");
            category.textContent = item.category;
            let count = document.createElement("td")
            count.textContent = "库存："+item.count;
            let inventoryCountAlert = document.createElement('td')
            inventoryCountAlert.textContent = "预警："+item.inventoryCountAlert
            row.appendChild(cell);
            row.appendChild(category)
            row.appendChild(count)
            row.appendChild(inventoryCountAlert)
            tbody.appendChild(row);
        });

        this.container.appendChild(table);
    }

    deconstructor(){
        while (this.container.firstChild) {
            this.container.removeChild((this.container.firstChild))
        }
    }
}
