import { LucideSquarePen, LucideTrash2, Pencil, Trash } from "lucide-react";

function BudgetElementActions({onDeleteButtonClick, onEditButtonClick}) {
    return (
        <div className="flex gap-1">
            <div className="cursor-pointer flex justify-center items-center p-1" onClick={onEditButtonClick}>
                <LucideSquarePen size={20} />
            </div>
            <div className="cursor-pointer flex justify-center items-center p-1" onClick={onDeleteButtonClick}>
                <LucideTrash2 size={19} color="red" />
            </div>
        </div>
    );
}

export default BudgetElementActions;