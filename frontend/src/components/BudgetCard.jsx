import { Eye, LucideSquarePen, LucideTrash2 } from "lucide-react";
import { formatCurrency } from "../utils/format";
import BudgetCardAction from "./BudgetCardAction";

function BudgetCard({budget, onViewButtonClick, onEditButtonClick, onDeleteButtonClick}) {
    return (
        <div className="p-4 border border-gray-200 hover:border-gray-300 rounded-lg transition-all duration-200">
            <div className="flex flex-col gap-3">
                <div>
                    <h3 className="font-semibold text-lg">
                        {budget.description}
                    </h3>
                </div>

                <div className="flex flex-col gap-2 justify-between text-sm md:flex-row">
                    <div className="flex flex-col">
                        <span className="text-gray-600">Número da proposta:</span>
                    
                        <span className="font-medium">{budget.proposalNumber}</span>
                    </div>

                    <div className="flex flex-col">
                        <span className="text-gray-600">Cliente:</span>
                    
                        <span className="font-medium">{budget.customer?.name || ""}</span>
                    </div>

                    <div className="flex flex-col">
                        <span className="text-gray-600">Total com BDI:</span>
                    
                        <span className="font-bold text-lg text-green-600">{formatCurrency(budget.totalWithBdi)}</span>
                    </div>

                    <div className="flex gap-2 h-1/3">
                        <BudgetCardAction icon={<Eye size={16}/>} onClick={onViewButtonClick}>
                            Ver
                        </BudgetCardAction>

                        <BudgetCardAction icon={<LucideSquarePen size={16}/>} onClick={onEditButtonClick}>
                            Editar
                        </BudgetCardAction>

                        <BudgetCardAction icon={<LucideTrash2 size={16}/>} onClick={onDeleteButtonClick} color="red">
                            Excluir
                        </BudgetCardAction>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default BudgetCard;