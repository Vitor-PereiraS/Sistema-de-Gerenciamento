if (window.PrimeFaces && PrimeFaces.widget.DataTable) {
    var originalOnRowClick = PrimeFaces.widget.DataTable.prototype.onRowClick;

    PrimeFaces.widget.DataTable.prototype.onRowClick = function(event, rowElement, silent) {
        // Verifica se é seleção múltipla ou checkbox; se for, mantém o comportamento original
        if (this.cfg.selectionMode === 'multiple' || this.cfg.selectionMode === 'checkbox') {
            if (originalOnRowClick) {
                originalOnRowClick.apply(this, arguments);
            }
            return;
        }

        // Para seleção única (single), aplica o clique alternado (seleciona / desmarca)
        if ($(event.target).is('td,span:not(.ui-c)')) {
            var row = $(rowElement),
                isSelected = row.hasClass('ui-state-highlight');

            if (isSelected) {
                this.unselectRow(row, silent);
                this.cursorIndex = null;
            } else {
                this.unselectAllRows(true);
                this.originRowIndex = row.index();
                this.cursorIndex = null;
                this.selectRow(row, silent);
            }

            PrimeFaces.clearSelection();
        }
    };
}
