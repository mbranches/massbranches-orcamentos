import { useNavigate } from "react-router-dom";
import BudgetStatus from "./BudgetStatus";
import { formatCurrency, formatDate } from "../utils/format";

function CustomerBudget({ budget }) {
    const navigate = useNavigate();

    return (
        <div 
            className="p-4 border border-gray-200 hover:shadow-md rounded-lg transition-all duration-200 cursor-pointer"
            onClick={() => {navigate(`/orcamentos/${budget.id}`)}}
        >
            <div className="flex flex-col gap-3">
                <div className="flex gap-2 items-center">
                    <h3 className="font-semibold text-md sm:text-lg">
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
                </div>
            </div>
        </div>
    );
}

export default CustomerBudget;