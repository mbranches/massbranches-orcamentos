import { useEffect, useState } from 'react';
import PanelLayout from '../components/PanelLayout'
import {exportBudgetById, findBudgetById, listElementsByBudgetId} from '../services/budget';
import { useNavigate, useParams } from 'react-router-dom';
import LoadingScreen from '../components/LoadingScreen'
import statusValidate from '../utils/statusValidate';
import Action from '../components/Action'
import { Box, ChevronLeft, FileDown, Info, ListPlus, LucideSquarePen } from 'lucide-react';
import BudgetTable from '../components/BudgetTable';
import { formatCurrency } from '../utils/format';
import Card from '../components/Card';
import { toast } from 'react-toastify';
import BudgetStatus from '../components/BudgetStatus';

function Budget() {
    const [newStage, setNewStage] = useState();
    const [newItem, setNewItem] = useState();
    const [sidebarOpen, setSidebarOpen] = useState();
    const [currentBudget, setCurrentBudget] = useState();
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { id } = useParams();
    const [elements, setElements] = useState([]);

    const fetchBudget = async () => {
        const foundBudget = await findBudgetById(id);

        setCurrentBudget(foundBudget.data);
    };
    const fetchBudgetElements = async () => {
        const foundElements = await listElementsByBudgetId(id);

        setElements(foundElements.data);
    };
    
    const newStageOrder = () => {
        if(elements.length === 0) return "1.0";

        const lastOrder = elements[elements.length - 1].order
        
        const [integerPart] = lastOrder.split(".");

        return `${parseInt(integerPart) + 1}.0`;
    }

    const newItemOrder = () => {
        const stages = elements.filter(element => element.type === "STAGE");

        if(stages.length === 0) return "1.0";

        const lastStageOrder = stages[stages.length - 1].order
        
        const [lastStageIntegerPart] = lastStageOrder.split(".");

        const elementsInTheStage = elements.filter(element => {
            const [integerPart] = element.order.split(".");
            
            return integerPart === lastStageIntegerPart;
        });

        const itemsInTheStage = elementsInTheStage.filter(element => element.type === "ITEM");

        if(itemsInTheStage.length === 0) return `${lastStageIntegerPart}.1`;

        const lastItem = itemsInTheStage[itemsInTheStage.length - 1];

        const [ lastItemIntegerPart, lastItemDecimalPart ] = lastItem.order.split(".");

        return `${lastStageIntegerPart}.${parseInt(lastItemDecimalPart) + 1 }`;
    };

    const handleNewStage = () => {
        setNewStage({
            order: newStageOrder(),
            name: ""
        });
    };

    const handleNewItem = () => {
        setNewItem({
            order: newItemOrder(),
            item: {
                id: "",
                name: "",
                unitMeasurement: "",
                unitPrice: "",
            },
            unitPrice: "",
            quantity: ""
        }); 
    };

    const onBudgetExportButtonClick = async() => {
        try {
            await exportBudgetById(id);

            toast.success("Orçamento exportado com sucesso!");
        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro ao exportar o orçamento, por favor tente novamente");

            statusValidate(status);
        };
    }

    const actions = [
        {
            label: "Adicionar Etapa",
            icon: <ListPlus size={18}  />,
            onClick: handleNewStage,
            key: "adicionar-etapa"
        },
        {
            label: "Adicionar Item",
            icon: <Box size={18}  />,
            onClick: handleNewItem,
            key: "adicionar-item"
        },
        {
            label: "Editar dados",
            icon: <LucideSquarePen size={18}  />,
            onClick: () => {navigate(`/orcamentos/${id}/dados`)},
            key: "dados-orcamento"
        },
        {
            label: "Exportar Orçamento",
            icon: <FileDown size={18}  />,
            onClick: () => {onBudgetExportButtonClick()},
            key: "exportar-orcamento"
        },
    ]

    useEffect(() => {
        setLoading(true);
        const fetchData = async () => {
            try {
                await fetchBudget();

                await fetchBudgetElements();
            } catch (error) {
                const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                                
                statusValidate(status);

                if(status === 404) {
                    navigate("/*");
                }
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    return (
        <div className="bg-gray-100">
            <PanelLayout setSidebarOpen={setSidebarOpen} sidebarOpen={sidebarOpen} actualSection={"visualizar-orcamentos"}>
            {loading && <LoadingScreen />}

                <div className='lg:ml-[310px] p-4 mt-[90px] min-h-screen flex flex-col gap-4 w-full'>
                    <div className='flex items-center gap-5'>
                        <div 
                            className='cursor-pointer rounded-md hover:bg-gray-200 p-1' 
                            onClick={() => navigate('/orcamentos')}
                        >
                            <ChevronLeft />
                        </div>

                        <div className='flex justify-between items-center w-full'>
                            <div>
                                <h2 className='text-3xl font-bold'>Detalhamento do Orçamento</h2>
                                <p className='text-gray-600'>{currentBudget?.description} - {currentBudget?.proposalNumber}</p>
                            </div>
                            <div>
                                <BudgetStatus status={currentBudget?.status}/>
                            </div>
                        </div>
                    </div>

                    <div className='grid grid-cols-1 md:grid-cols-4 gap-4 p-6 bg-white rounded-lg'>
                        {actions.map(action => {
                            return (
                                <Action icon={action.icon} label={action.label} onClick={action.onClick} key={action.key} />
                            );
                        })}
                    </div>  

                    <div>
                        <BudgetTable 
                            elements={elements} 
                            newItem={newItem} 
                            setNewItem={setNewItem} 
                            newStage={newStage} 
                            setNewStage={setNewStage} 
                            fetchBudgetElements={fetchBudgetElements} 
                            setLoading={setLoading} 
                            currentBudget={currentBudget} 
                            fetchBudget={fetchBudget}
                        />
                    </div>
                    
                    <div className='grid grid-cols-1 md:grid-cols-2 gap-6'>
                        <Card backgroud={"bg-white"}>
                            <div className='flex justify-between'>
                                <span>Subtotal:</span>

                                <span className='font-bold text-2xl'>
                                    {formatCurrency(currentBudget?.totalValue)}
                                </span>
                            </div>

                            <div className='flex justify-between'>
                                <div className='flex gap-2'>
                                    <span>BDI:</span>

                                    <span>{currentBudget?.bdi}%</span>
                                </div>

                                <div>
                                    <span className='text-blue-600 font-semibold'>
                                        {formatCurrency(currentBudget?.totalWithBdi - currentBudget?.totalValue)}
                                    </span>
                                </div>
                            </div>
                        </Card>

                        <Card backgroud={"bg-green-100"}>
                            <div className='flex justify-between'>
                                <span className='text-green-800 font-bold'>
                                    Total com BDI:
                                </span>

                                <span className='text-2xl text-green-900 font-bold'>
                                    {formatCurrency(currentBudget?.totalWithBdi)}
                                </span>
                            </div>
                        </Card>
                    </div>
                </div>
            </PanelLayout>
        </div>
    )
}

export default Budget;