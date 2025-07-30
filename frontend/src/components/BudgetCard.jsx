import { Eye, LucideSquarePen, LucideTrash2 } from "lucide-react";
import { formatCurrency, formatDate } from "../utils/format";
import CardAction from "./CardAction";
import BudgetStatus from "./BudgetStatus";

function BudgetCard({budget, onViewButtonClick, onEditButtonClick, onDeleteButtonClick, onCardClick}) {
    return (
        <div 
            className="p-4 border border-gray-200 hover:shadow-md cursor-pointer rounded-lg transition-all duration-200"
            onClick={onCardClick}    
        >
            <div className="flex flex-col gap-3">
                <div className="flex gap-2 items-center">
                    <h3 className="font-semibold text-lg">
                        {budget.description}
                    </h3>

                    <div>
                        <BudgetStatus status={budget.status} />
                    </div>
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
                        <span className="text-gray-600">Data de criação:</span>
                    
                        <span className="font-medium">{formatDate(budget.createdAt) || "00/00/00"}</span>
                    </div>

                    <div className="flex flex-col">
                        <span className="text-gray-600">Total com BDI:</span>
                    
                        <span className="font-bold text-green-600">{formatCurrency(budget.totalWithBdi)}</span>
                    </div>

                    <div 
                        className="flex gap-2 h-1/3"
                        onClick={e => e.stopPropagation()}
                    >
                        <CardAction icon={<Eye size={16}/>} onClick={onViewButtonClick}>
                            Ver
                        </CardAction>

                        <CardAction icon={<LucideSquarePen size={16}/>} onClick={onEditButtonClick}>
                            Editar dados
                        </CardAction>

                        <CardAction icon={<LucideTrash2 size={16}/>} onClick={onDeleteButtonClick} color="red">
                            Excluir
                        </CardAction>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default BudgetCard;