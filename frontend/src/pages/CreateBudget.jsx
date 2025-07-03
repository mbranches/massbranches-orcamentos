import PanelLayout from '../components/PanelLayout';
import BudgetForm from '../components/BudgetForm';
import { useEffect, useState } from 'react';
import listAllCustomers from '../services/Customer';
import LoadingScreen from '../components/LoadingScreen';
import {ToastContainer} from 'react-toastify';
import {createBudget} from "../services/budget";
import { useAuth } from '../hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import statusValidate from '../Utils/statusValidate';

function CreateBudget() {
    const [ sidebarOpen, setSidebarOpen ] = useState();

    const [ loading, setLoading ] = useState(false);

    const [ customers, setCustomers ] = useState([]);

    const { isAdmin } = useAuth();

    const navigate = useNavigate();

    useEffect(() => async () => {
        setLoading(true);

        try {
            const customers = isAdmin? await listAllCustomers(true) : await listAllCustomers();

            setCustomers(customers);
        } catch(error) {
            const status = error.response?.status;

            statusValidate(status);
        } finally {
            setLoading(false);
        }
    }, [])

    const onFormSubmit = async (data) => {
        setLoading(true);

        try {
            const customerId = data.customer?.id;

            const response = await createBudget(customerId, data.description, data.proposalNumber, data.bdi);

            const createdBudget = response.data;

            navigate(`/orcamentos/${createdBudget.id}`)
        } catch(error) {
            const status = error?.response?.status;

            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-gray-100">
            <PanelLayout actualSection={"criar-orcamento"} sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}>
                <div className='flex flex-col w-full h-screen justify-center md:w-3/4 md:h-auto lg:w-1/2 lg:ml-[310px] px-5 py-8 bg-white rounded-lg'>
                    <div className='mb-4'>
                        <h3 className='text-2xl'>
                            Criar Orçamento
                        </h3>
                    </div>

                    <BudgetForm customers={customers} onSubmit={onFormSubmit} />
                </div>
            </PanelLayout>

            {loading && <LoadingScreen />}

            <ToastContainer autoClose={3000} pauseOnHover={false} position='top-right'/>
        </div>
    );
}

export default CreateBudget;