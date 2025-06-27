import { useForm } from 'react-hook-form';
import PanelLayout from '../components/PanelLayout';
import FormTextField from '../components/FormTextField';
import FormSelect from '../components/FormSelect';
import { useEffect, useState } from 'react';
import listAllCustomers from '../services/Customer';
import LoadingScreen from '../components/LoadingScreen';
import {toast, ToastContainer} from 'react-toastify';
import createBudget from "../services/Budget";
import { useAuth } from '../hooks/useAuth';

function CreateBudget() {
    const [ sidebarOpen, setSidebarOpen ] = useState();

    const { control, register, handleSubmit, formState: { errors }} = useForm();

    const [ loading, setLoading ] = useState(false);

    const [ customers, setCustomers ] = useState([]);

    const { isAdmin } = useAuth();

    useEffect(() => async () => {
        setLoading(true);

        try {
            const customers = isAdmin? await listAllCustomers(true) : await listAllCustomers();

            setCustomers(customers);
        } catch(error) {
            const status = error.response?.status;
            
            if (status === 401 || status === 403) {
                toast.error("Conexão expirada, por favor faça login novamente")
            } else {
                toast.error("Ocorreu um erro interno, por favor tente novamente");
            }
        } finally {
            setLoading(false);
        }
    }, [])

    const onSubmit = (data) => {
        setLoading(true);

        try {
            const customerId = data.customer.value;

            createBudget(customerId, data.description, data.proposalNumber, data.bdi);
        } catch {

        } finally {
            setLoading(false);
        }
    };
    
    const showRequiredErrorMessage = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Campo obrigatório.</p>
    );

    const showBdiInvalidError = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Digite um BDI válido.</p>
    );

    return (
        <div className="bg-slate-200">
            <PanelLayout actualSection={"criar-orcamento"} sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}>
                <div className='flex flex-col w-full h-screen justify-center md:w-3/4 md:h-auto lg:w-1/2 lg:ml-[310px] px-5 py-8 bg-white rounded-lg'>
                    <div className='mb-4'>
                        <h3 className='text-2xl'>
                            Criar Orçamento
                        </h3>
                    </div>

                    <form onSubmit={handleSubmit(onSubmit)} className='space-y-6'>
                        <div>    
                            <FormTextField
                                type={"text"}
                                id={"description"}
                                label={"Descrição"} 
                                placeholder={"Descrição do Orçamento"} 
                                register={register('description', {required: true})} 
                            />
                        </div>

                        {errors?.description?.type === "required" && showRequiredErrorMessage()}

                        <div>
                            <FormSelect 
                                id={"customer"}
                                name="customer"
                                label={"Cliente"}
                                control={control}
                                options={customers}
                            />
                        </div>

                        <div>
                            <FormTextField 
                                type={"text"}
                                id={"proposalNumber"}
                                label={"Número da Proposta"}
                                placeholder={"Ex: \"2025/0072\""}
                                register={register('proposalNumber', {required: true})}
                            />
                        </div>

                        {errors?.proposalNumber?.type === "required" && showRequiredErrorMessage()}

                        <div>
                            <FormTextField 
                                type={"text"}
                                id={"bdi"}
                                label={"BDI (%)"}
                                placeholder={"Ex: \"18.76\""}
                                register={register('bdi', {pattern: {
                                    value: /^\d+([.,]\d{1,2})?$/
                                }})}
                            />
                        </div>

                        {errors?.bdi?.type === "required" && showRequiredErrorMessage() || errors?.bdi?.type === "pattern" && showBdiInvalidError()}

                        <div className="flex justify-end">
                            <button type="submit" className='w-full py-4 px-10 md:py-2 md:w-auto border border-slate-300 hover:border-slate-400 rounded-lg text-slate-700 outline-none cursor-pointer'>Criar</button>
                        </div>
                    </form>
                </div>
            </PanelLayout>

            {loading && <LoadingScreen />}

            <ToastContainer autoClose={3000} pauseOnHover={false} position='top-right'/>
        </div>
    );
}

export default CreateBudget;