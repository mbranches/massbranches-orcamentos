import { X } from "lucide-react";
import Customer from "./Customer";
import { useEffect, useState } from "react";
import BudgetCard from './BudgetCard';
import LoadingScreen from './LoadingScreen';
import { toast } from "react-toastify";
import statusValidate from "../utils/statusValidate";
import { listBudgetsByCustomer } from "../services/customer";
import CustomerBudget from "./CustomerBudget";

function CustomerDetails({ customer, setModalIsOpen }) {
    const [ budgetsOfCustomer, setBudgetsOfCustomer ] = useState([]);

    const [ loading, setLoading ] = useState(false);

    const fetchBudgetsOfCustomer = async () => {
        setLoading(true);

        try {
            const response = await listBudgetsByCustomer(customer);

            setBudgetsOfCustomer(response.data);
        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente");

            statusValidate(status);
        } finally {
            setLoading(false)
        }
    };

    useEffect(() => {
        fetchBudgetsOfCustomer();
    }, []);

    return (
        <div 
            className="flex flex-col gap-4"
            onClick={(e) => e.stopPropagation()}
        >
            {loading && <LoadingScreen />}
            <div className="flex justify-between items-center">
                    <h3 className="text-2xl font-bold">
                        Detalhes do Cliente
                    </h3>

                    <div 
                        className="hover:bg-gray-100 p-1 rounded-md cursor-pointer"
                        onClick={() => setModalIsOpen(false)}
                    >
                        <X size={18}/>
                    </div>
            </div>

            <div>
                <Customer customer={customer} />
            </div>

            <div>
                <h4 className="font-semibold text-lg">
                    Orçamentos ({customer.numberOfBudgets})
                </h4>
            </div>

            <div className="flex flex-col px-2 gap-3 max-h-56 overflow-auto">
                {
                    budgetsOfCustomer.map(budget =>
                        <CustomerBudget key={budget.id} budget={budget}/>
                    )
                }
            </div>
        </div>
    );
}

export default CustomerDetails;