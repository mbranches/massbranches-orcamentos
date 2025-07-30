import { useEffect, useState } from 'react';
import PanelLayout from '../components/PanelLayout';
import { deleteBudgetById, findAllMyBudgets, filterMyBudgets } from '../services/budget';
import { useNavigate } from 'react-router-dom';
import BudgetCard from '../components/BudgetCard';
import LoadingScreen from '../components/LoadingScreen';
import statusValidate from '../utils/statusValidate';
import { toast } from 'react-toastify';
import FilterSelect from '../components/FilterSelect';
import SearchBar from '../components/SearchBar';
import ButtonNew from '../components/ButtonNew';
import DeleteConfirmationBox from '../components/DeleteConfirmationBox';
import Modal from '../components/Modal';
import BudgetDetails from '../components/BudgetDetails';

function Budgets() {
    const [loading, setLoading] = useState(false);

    const [budgets, setBudgets] = useState([]);

    const [sidebarOpen, setSidebarOpen] = useState(false);

    const [refreshBudgets, setRefreshSidebar] = useState(0);

    const [deleteConfirmationBoxIsOpen, setDeleteConfirmationBoxIsOpen] = useState(false);

    const [budgetToDelete, setBudgetToDelete] = useState();

    const [budgetToViewDetails, setBudgetToViewDetails] = useState(null);

    const [budgetDetailsModalIsOpen, setBudgetDetailsModalIsOpen] = useState(false)

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

    const onBudgetCardClick = budget => {
        setBudgetToViewDetails(budget);
        setBudgetDetailsModalIsOpen(true);
    }

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
        
                    <div>
                        <ButtonNew label={'Novo Orçamento'} onClick={() => navigate("/orcamentos/criar")} />
                    </div>
                </div>

                <div className='flex flex-col sm:flex-row w-full gap-3'>
                    <div className='sm:w-4/5 w-full'>
                        <SearchBar placeholder={'Buscar por descrição do orçamento'} onSearch={(e) => filterBudgets(e.target.value, statusToSearch)} />
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
                                onCardClick={() => onBudgetCardClick(budget)}
                            />
                        );
                    })) || (
                        <div className='flex justify-center items-center h-96'>
                            <p className='text-gray-600'>Nenhum orçamento encontrado</p>
                        </div>
                    )}
                </div>
            </div>

            <DeleteConfirmationBox 
                deleteConfirmationBoxIsOpen={deleteConfirmationBoxIsOpen} 
                setDeleteConfirmationBoxIsOpen={setDeleteConfirmationBoxIsOpen} 
                objectToDelete={budgetToDelete?.description}
                onDelete={() => deleteBudget(budgetToDelete)}
            />

            {budgetDetailsModalIsOpen && (
                <Modal modalIsOpen={budgetDetailsModalIsOpen} setModalIsOpen={setBudgetDetailsModalIsOpen} className={"w-2/3 md:w-2/5"}>
                    <BudgetDetails 
                        budget={budgetToViewDetails}
                        setModalIsOpen={setBudgetDetailsModalIsOpen}
                    />
                </Modal>
            )}
        </PanelLayout>
    );         
        
}

export default Budgets;