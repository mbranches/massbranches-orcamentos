import PanelLayout from '../components/PanelLayout';
import BudgetForm from '../components/BudgetForm';
import { useState } from 'react';
import LoadingScreen from '../components/LoadingScreen';
import {createBudget} from "../services/budget";
import { useNavigate } from 'react-router-dom';
import statusValidate from '../utils/statusValidate';
import { toast } from 'react-toastify';
import { formatDecimal } from '../utils/format';

function CreateBudget() {
    const [ sidebarOpen, setSidebarOpen ] = useState();

    const [ loading, setLoading ] = useState(false);

    const navigate = useNavigate();

    const onFormSubmit = async (data) => {
        setLoading(true);

        try {
            const customerId = data.customer?.value;
            const status = data.status.value;

            const response = await createBudget(customerId, data.description, data.proposalNumber, status, formatDecimal(data.bdi));

            const createdBudget = response.data;

            toast.success("Orçamento criado com sucesso")
            navigate(`/orcamentos/${createdBudget.id}`);
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-gray-100">
            <PanelLayout actualSection={"criar-orcamento"} sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}>
                <div className='flex flex-col w-full h-screen justify-center md:w-3/4 md:h-auto lg:w-1/2 lg:ml-[310px] mt-[83px] px-5 py-8 bg-white rounded-lg'>
                    <div className='mb-4'>
                        <h3 className='text-2xl'>
                            Criar Orçamento
                        </h3>
                    </div>

                    <BudgetForm submitButtonLabel={"Criar"} defaultValues={{status: { value: "EM_ANDAMENTO", label: "Em andamento" }}} onSubmit={onFormSubmit} setLoading={setLoading} />
                </div>
            </PanelLayout>

            {loading && <LoadingScreen />}

        </div>
    );
}

export default CreateBudget;