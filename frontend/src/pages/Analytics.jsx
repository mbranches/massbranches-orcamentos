import { useState } from "react";
import PanelLayout from "../components/PanelLayout";
import AnalysisCard from "../components/AnalysisCard";
import { CarTaxiFront, ChartBarIncreasing, DollarSign, TrendingUp } from "lucide-react";
import { getBudgetConversionRate, getBudgetConversionRateByCustomerType, getTopCustomers, getTotalApproved, getTotalBudgeted } from "../services/analytics";
import { toast } from "react-toastify";
import statusValidate from "../Utils/statusValidate";
import Th from "../components/Th";
import { formatCurrency } from "../utils/format";
import LoadingScreen from "../components/LoadingScreen";

function Analytics() {
    const [sidebarOpen, setSidebarOpen] = useState();
    const [loading, setLoading] = useState();
    const [budgetConversionRate, setBudgetConversionRate] = useState(0.00);
    const [budgetConversionRateByCustomerType, setBudgetConversionRateByCustomerType] = useState([]);
    const [topCustomers, setTopCustomers] = useState([]);
    const [totalBudgeted, setTotalBudgeted] = useState(0);
    const [totalApproved, setTotalApproved] = useState(0);

    const fetchAnalysis = async() => {
        setLoading(true);

        try {
            const budgetConversionRateResponse = await getBudgetConversionRate();
            setBudgetConversionRate(budgetConversionRateResponse.data);
            
            const budgetConversionRateByCustomerTypeResponse = await getBudgetConversionRateByCustomerType();
            setBudgetConversionRateByCustomerType(budgetConversionRateByCustomerTypeResponse.data);
            
            const topCustomersResponse = await getTopCustomers();
            setTopCustomers(topCustomersResponse.data);
            
            const totalBudgetedResponse = await getTotalBudgeted();
            setTotalBudgeted(totalBudgetedResponse.data);
            
            const totalApprovedResponse = await getTotalApproved();
            setTotalApproved(totalApprovedResponse.data);

        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
            
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const customerTypeMap = {
        "PESSOA_FISICA": "Pessoa Física",
        "PESSOA_JURIDICA": "Pessoa Jurídica",
    }

    useState(() => {
        fetchAnalysis();
    }, [])

    return (
        <PanelLayout setSidebarOpen={setSidebarOpen} sidebarOpen={sidebarOpen}>
            {loading && <LoadingScreen />}

            <div className='w-full min-h-screen mt lg:ml-[310px] mt-[83px] p-4 flex flex-col gap-5'>
                <div>
                    <h2 className='text-3xl font-bold mb-1'>Minhas Análises</h2>
                </div>

                <div className="flex flex-col gap-20">
                    <div className="flex flex-col gap-3">
                        <h3 className="text-xl font-semibold">Análises de orçamentos</h3>

                        <div className="flex flex-col gap-5">
                            <AnalysisCard>
                                <div className="flex flex-col gap-1">
                                    <span className="text-sm text-slate-400">
                                        Porcentagem de orçamentos aprovados 
                                    </span>

                                    <div className="flex items-center justify-between gap-8">
                                        <span className="text-xl font-semibold">
                                            % de Aprovação 
                                        </span>

                                        <div>
                                            <TrendingUp size={20} />
                                        </div>
                                    </div>
                                </div>

                                <div>
                                    <span className="font-medium text-xl">
                                        {`${budgetConversionRate}%`}
                                    </span>
                                </div>
                            </AnalysisCard>

                            <AnalysisCard>
                                <div className="flex flex-col gap-1">
                                    <span className="text-sm text-slate-400">
                                        Valor total com BDI orçado 
                                    </span>

                                    <div className="flex items-center justify-between gap-8">
                                        <span className="text-xl font-semibold">
                                            Total orçado 
                                        </span>

                                        <div>
                                            <DollarSign size={20} />
                                        </div>
                                    </div>
                                </div>

                                <div>
                                    <span className="font-medium text-xl">
                                        {formatCurrency(totalBudgeted)}
                                    </span>
                                </div>
                            </AnalysisCard>

                            <AnalysisCard>
                                <div className="flex flex-col gap-1">
                                    <span className="text-sm text-slate-400">
                                        Valor total com BDI aprovado 
                                    </span>

                                    <div className="flex items-center justify-between gap-8">
                                        <span className="text-xl font-semibold">
                                            Total aprovado 
                                        </span>

                                        <div>
                                            <DollarSign size={20} />
                                        </div>
                                    </div>
                                </div>

                                <div>
                                    <span className="font-medium text-xl text-green-600">
                                        {formatCurrency(totalApproved)}
                                    </span>
                                </div>
                            </AnalysisCard>
                        </div>
                    </div>

                    <div className="flex flex-col gap-3">
                        <h3 className="text-xl font-semibold">Análises de clientes</h3>

                        <div className="flex flex-col gap-5">
                            <AnalysisCard>
                                <div className="flex flex-col gap-3">
                                    <div className="flex flex-col gap-1">
                                        <span className="text-sm text-slate-400">
                                            Comparação PF x PJ 
                                        </span>

                                        <div className="flex items-center justify-between gap-8">
                                            <span className="text-xl font-semibold">
                                                % de Aprovação 
                                            </span>

                                            <div>
                                                <TrendingUp size={20} />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="flex flex-col gap-2">
                                        {budgetConversionRateByCustomerType.map(byType => (
                                            <div className="flex justify-between gap-50">
                                                <div>{customerTypeMap[byType.type]}</div>

                                                <div>{`${byType.conversionRate}%`}</div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </AnalysisCard>

                            <AnalysisCard>
                                <div className="flex flex-col gap-3">
                                    <div className="flex flex-col gap-1">
                                        <span className="text-sm text-slate-400">
                                            Clientes com mais orçamentos
                                        </span>

                                        <div className="flex items-center justify-between gap-8">
                                            <span className="text-xl font-semibold">
                                                Top 5 clientes
                                            </span>

                                            <div>
                                                <ChartBarIncreasing size={20} />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="overflow-auto">
                                    <table className="min-w-md w-full">
                                        <thead>
                                            <tr>
                                                <Th>Cliente</Th>  
                                                <Th>Tipo do Cliente</Th>  
                                                <Th>Orçamentos solicitados</Th>  
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {topCustomers.map(customer => (
                                                <tr className="border-b border-slate-200">
                                                    <td className="text-left">{customer.name}</td>
                                                    <td className="text-left p-2 pl-2">{customerTypeMap[customer.type]}</td>
                                                    <td className="text-left pl-10">{customer.numberOfBudgets}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                    </div>
                                </div>
                            </AnalysisCard>
                        </div>
                    </div>
                </div>
            </div>
        </PanelLayout>
    );
}

export default Analytics;