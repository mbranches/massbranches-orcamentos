import { Building2, LucideUser } from "lucide-react";
import CustomerType from "./CustomerType";
import { formatCurrency } from "../utils/format";

function Customer({ customer }) {
    return (
        <div className="bg-gray-100 flex flex-col gap-4 p-6 rounded-lg">
            <div className="flex gap-3">
                <div className="rounded-full text-slate-600 bg-gray-200 h-13 w-13 p-2 flex items-center justify-center">
                    {customer.type === "PESSOA_JURIDICA" ? <Building2 size={32} /> : <LucideUser size={32} />}
                </div>

                <div>
                    <h4 className="text-xl font-bold">{customer.name}</h4>
                    <div>
                        <CustomerType type={customer.type} />
                    </div>
                </div>
            </div>

            <div>
                <div className="flex flex-col gap-2 justify-between text-sm md:flex-row">
                    <div className="flex flex-col">
                        <span className="text-gray-600">Total de orçamentos:</span>
                    
                        <span className="font-bold">
                            {`${customer.numberOfBudgets} orçamentos`}
                        </span>
                    </div>

                    <div className="flex flex-col">
                        <span className="text-gray-600">Total em orçamentos com BDI:</span>
                    
                        <span className="font-bold text-green-600">
                            {formatCurrency(customer.totalInBudgetsWithBdi)}
                        </span>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Customer;