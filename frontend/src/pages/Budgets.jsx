import { useEffect, useState } from 'react';
import PanelLayout from '../components/PanelLayout';
import { deleteBudgetById, findAllMyBudgets, filterMyBudgets } from '../services/budget';
import { Plus, Search } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import BudgetCard from '../components/BudgetCard';
import LoadingScreen from '../components/LoadingScreen';
import statusValidate from '../utils/statusValidate';
import { toast } from 'react-toastify';
import FilterSelect from '../components/FilterSelect';

function Budgets() {
    const [loading, setLoading] = useState(false);

    const [budgets, setBudgets] = useState([]);

    const [sidebarOpen, setSidebarOpen] = useState(false);

    const [refreshBudgets, setRefreshSidebar] = useState(0);

    const [deleteConfirmationBoxIsOpen, setDeleteConfirmationBoxIsOpen] = useState(false);

    const [budgetToDelete, setBudgetToDelete] = useState();

    const navigate = useNavigate();

    const [descriptionToSearch, setDescriptionToSearch] = useState();
    
    const [statusToSearch, setStatusToSearch] = useState();

    const deleteBudget = async (budget) => {
        setLoading(true);

        try {
            await deleteBudgetById(budget.id);

            setRefreshSidebar(refreshBudgets + 1);

            setDeleteConfirmationBoxIsOpen(false);
            setBudgetToDelete(null);

            toast.success("Orçamento deletado com sucesso");
        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const fetchBudgets = async() => {
        setLoading(true);

        try {
            const response = await findAllMyBudgets();

            setBudgets(response.data);
        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
            
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const filterBudgets = async (description, status) => {
        setDescriptionToSearch(description);
        setStatusToSearch(status);

        if(description?.trim() === "" && (status === "all" || !status)) {
            fetchBudgets();
            return;
        }

        try {
            const response = await filterMyBudgets(
                description?.trim() || null,
                status === "all" ? null : status
            );

            setBudgets(response.data);
        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
            
            statusValidate(status);
        }
    };

    const budgetStatusFilterOptions = [
        { value: "all", label: "Todos" },
        { value: "EM_ANDAMENTO", label: "Em andamento" },
        { value: "EM_ANALISE", label: "Em análise" },
        { value: "APROVADO", label: "Aprovado" },
        { value: "RECUSADO", label: "Recusado" }
    ]

    const onDeleteBudgetButtonClick = (budget) => {
        setDeleteConfirmationBoxIsOpen(true);
        setBudgetToDelete(budget);
    };

    useEffect(() => {
        fetchBudgets();
    }, [refreshBudgets])

    return (
        <PanelLayout actualSection={"visualizar-orcamentos"} setSidebarOpen={setSidebarOpen} sidebarOpen={sidebarOpen} refreshSidebar={refreshBudgets}>
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

                <div className='flex flex-col sm:flex-row w-full gap-3'>
                    <div className='flex items-center border border-gray-300 rounded-md p-2 w-full sm:w-4/5'>
                        <div className='flex items-center justify-center px-2'>
                            <Search size={15} />
                        </div>

                        <input type="text" placeholder='Buscar por descrição do orçamento' className='block text-sm w-full px-1 py-1 outline-none' onChange={(e) => {
                            filterBudgets(e.target.value, statusToSearch);
                        }}/>
                    </div>

                    <div className='w-full sm:w-1/5'>
                        <FilterSelect options={budgetStatusFilterOptions} selected={budgetStatusFilterOptions[0]} value={"status"} onChange={(e) => filterBudgets(descriptionToSearch, e.target.value)}/>
                    </div>
                </div>

                <div className='flex flex-col gap-4 mt-4'>
                    {
                    (budgets.length > 0 && budgets.map(budget => {
                        return ( 
                            <BudgetCard 
                                key={budget.id} 
                                budget={budget} 
                                onViewButtonClick={() => navigate(`/orcamentos/${budget.id}`)}
                                onEditButtonClick={() => navigate(`/orcamentos/${budget.id}/dados`)}
                                onDeleteButtonClick={() => onDeleteBudgetButtonClick(budget)} 
                            />
                        );
                    })) || (
                        <div className='flex justify-center items-center h-96'>
                            <p className='text-gray-600'>Nenhum orçamento encontrado</p>
                        </div>
                    )}
                </div>
            </div>

            {deleteConfirmationBoxIsOpen && (
                <div 
                    className='fixed inset-0 bg-black/20 flex justify-center items-center'
                    onClick={() => setDeleteConfirmationBoxIsOpen(false)}
                >
                    <div 
                        className='lg:ml-[310px] flex justify-center items-center flex-col gap-4 bg-white px-4 py-6 rounded-md shadow-lg'
                        onClick={(e) => e.stopPropagation()}
                    >
                        <div>
                            <h3>{`Excluir ${budgetToDelete.description} ?`}</h3>
                            <p className='text-sm text-gray-600'>Essa ação não poderá ser desfeita</p>
                        </div>

                        <div className='flex gap-3'>
                            <button 
                                onClick={() => setDeleteConfirmationBoxIsOpen(false)}
                                className='border border-gray-200 text-sm px-4 py-2 rounded-md hover:border-gray-300 transition-all duration-100 cursor-pointer'
                            >
                                Cancelar
                            </button>

                            <button 
                                onClick={() => deleteBudget(budgetToDelete)}
                                className='bg-red-500 text-white text-sm px-4 py-2 rounded-md hover:bg-red-600 transition-all duration-100 cursor-pointer'
                            >
                                Confirmar
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </PanelLayout>
    );         
        
}

export default Budgets;