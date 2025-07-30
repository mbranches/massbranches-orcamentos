import { X } from "lucide-react";
import { formatCurrency, formatDate } from "../utils/format";
import BudgetStatus from "./BudgetStatus";

function BudgetDetails({ budget, setModalIsOpen }) {
    return (
        <div className="flex flex-col gap-4 rounded-lg">
            <div className="flex justify-between items-center">
                <div className="flex items-center gap-2">
                    <h4 className="text-xl font-bold">{budget.description}</h4>

                    <div>
                        <BudgetStatus status={budget.status} />
                    </div>
                </div>

                <div 
                    className="hover:bg-gray-100 p-1 rounded-md cursor-pointer"
                    onClick={() => setModalIsOpen(false)}
                >
                    <X size={18}/>
                </div>
            </div>

            <div className="grid sm:grid-cols-2 gap-4 justify-between text-sm">
                <div className="flex flex-col">
                    <span className="text-gray-600">Número da proposta:</span>
                
                    <span className="font-bold">
                        {budget.proposalNumber}
                    </span>
                </div>

                <div className="flex flex-col">
                    <span className="text-gray-600">Cliente:</span>
                
                    <span className="font-bold">
                        {budget.customer.name}
                    </span>
                </div>

                <div className="flex flex-col">
                    <span className="text-gray-600">BDI:</span>
                
                    <span className="font-bold">
                        {`${budget.bdi}%`}
                   </span>
                </div>

                <div className="flex flex-col">
                    <span className="text-gray-600">Subtotal:</span>
                
                    <span className="font-bold text-blue-600">
                        {formatCurrency(budget.totalValue)}
                   </span>
                </div>

                <div className="flex flex-col">
                    <span className="text-gray-600">Total com BDI:</span>
                
                    <span className="font-bold text-green-600">
                        {formatCurrency(budget.totalWithBdi)}
                   </span>
                </div>

                <div className="flex flex-col">
                    <span className="text-gray-600">Data de criação:</span>
                
                    <span className="font-bold">
                        {formatDate(budget.createdAt)}
                   </span>
                </div>
            </div>
        </div>
    )
}

export default BudgetDetails;