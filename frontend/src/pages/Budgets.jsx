import { useEffect, useState } from 'react';
import PanelLayout from '../components/PanelLayout';
import { deleteBudgetById, findAllBudgets } from '../services/budget';
import { Plus, Search } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import BudgetCard from '../components/BudgetCard';
import LoadingScreen from '../components/LoadingScreen';
import statusValidate from '../utils/statusValidate';
import { toast } from 'react-toastify';

function Budgets() {
    const [loading, setLoading] = useState(false);

    const [budgets, setBudgets] = useState([]);

    const [sidebarOpen, setSidebarOpen] = useState(false);

    const navigate = useNavigate();

    const deleteBudget = async (budget) => {
        setLoading(true);

        try {
            await deleteBudgetById(budget.id);
        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const fetchBudgets = async() => {
            setLoading(true);

            try {
                const response = await findAllBudgets();

                setBudgets(response.data);
            } catch (error) {
                const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
                statusValidate(status);
            } finally {
                setLoading(false);
            }
        };

        fetchBudgets();
    }, [])

    return (
        <PanelLayout actualSection={"budgets"} setSidebarOpen={setSidebarOpen} sidebarOpen={sidebarOpen}>
            {loading && <LoadingScreen />}

            <div className='w-full min-h-screen mt lg:ml-[310px] mt-[83px] p-4 flex flex-col gap-4'>
                <div className='flex flex-col gap-4 sm:flex-row justify-between sm:items-center bg-white'>
                    <div>
                        <h2 className='text-3xl font-bold mb-1'>Orçamentos</h2>
                        <p className='text-gray-600'>{budgets.length} orçamentos encontrados.</p>
                    </div>

                    <div className='bg-black w-full sm:w-auto text-white flex items-center px-4 py-2 rounded-sm h-10 gap-2 cursor-pointer hover:bg-gray-800 transition-all duration-100' onClick={() => navigate("/orcamentos/criar")}>
                        <div><Plus size={20} /></div>
                        <span className='text-sm'>Novo Orçamento</span>
                    </div>
                </div>

                <div className='flex items-center border border-gray-300 rounded-md p-2'>
                    <div className='flex items-center justify-center px-2'>
                        <Search size={15} />
                    </div>

                    <input type="text" placeholder='Buscar por descrição do orçamento' className='block text-sm w-full px-1 py-1 outline-none'/>
                </div>

                <div className='flex flex-col gap-4 mt-4'>
                    {budgets.map(budget => {
                        return ( 
                            <BudgetCard 
                                key={budget.id} 
                                budget={budget} 
                                onViewButtonClick={() => navigate(`/orcamentos/${budget.id}`)}
                                onEditButtonClick={() => navigate(`/orcamentos/${budget.id}/detalhes`)}
                                onDeleteButtonClick={() => deleteBudget(budget.id)} 
                            />
                        );
                    })}
                </div>
            </div>
        </PanelLayout>
    );
}

export default Budgets;