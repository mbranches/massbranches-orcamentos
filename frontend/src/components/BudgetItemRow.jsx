import { formatCurrency } from "../utils/format";
import BudgetItem from "./BudgetItem";
import BudgetItemData from "./BudgetItemData";
import BudgetElementActions from './BudgetElementActions';

function BudgetItemRow({ item, onDeleteButtonClick, onEditButtonClick }) {
    return(
        <BudgetItem>
            <BudgetItemData>{item?.order}</BudgetItemData>
            <BudgetItemData>{item?.name}</BudgetItemData>
            <BudgetItemData>{item?.unitMeasurement}</BudgetItemData>
            <BudgetItemData>{formatCurrency(item.unitPrice)}</BudgetItemData>
            <BudgetItemData>{item?.quantity}</BudgetItemData>
            <BudgetItemData>{formatCurrency(item.totalValue)}</BudgetItemData>
            <BudgetItemData>
                <BudgetElementActions onEditButtonClick={onEditButtonClick} onDeleteButtonClick={onDeleteButtonClick} />
            </BudgetItemData>
        </BudgetItem>
    );
}

export default BudgetItemRow;