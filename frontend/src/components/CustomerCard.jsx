import { Building2, Eye, LucideSquarePen, LucideTrash2, LucideUser, PersonStanding } from "lucide-react";
import { formatCurrency } from "../utils/format";
import CardAction from "./CardAction";
import CustomerType from "./CustomerType";

function CustomerCard({customer, onViewButtonClick, onEditButtonClick, onDeleteButtonClick}) {
    return (
        <div className="p-4 border border-gray-200 hover:border-gray-300 rounded-lg transition-all duration-200">
            <div className="flex flex-col gap-3">
                <div className="flex gap-2 items-center">
                    <div className="rounded-full text-slate-600 bg-gray-100 p-3">
                        {customer.type === "PESSOA_JURIDICA" ? <Building2 size={20} /> : <LucideUser size={20} />}
                    </div>

                    <div className="flex flex-col">
                        <h3 className="font-semibold text-lg">
                            {customer.name}
                        </h3>

                        <div>
                            <CustomerType type={customer.type} />
                        </div>
                    </div>
                </div>

                <div className="flex flex-col gap-2 justify-between text-sm md:flex-row">
                    <div className="flex flex-col">
                        <span className="text-gray-600">Total de orçamentos:</span>
                    
                        <span className="font-bold">
                            {`${customer.numberOfBudgets} orçamentos`}
                        </span>
                    </div>

                    <div className="flex flex-col">
                        <span className="text-gray-600">Total em orçamentos com BDI:</span>
                    
                        <span className="font-bold text-lg text-green-600">
                            {formatCurrency(customer.totalInBudgetsWithBdi)}
                        </span>
                    </div>

                    <div className="flex gap-2 h-1/3">
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

export default CustomerCard;