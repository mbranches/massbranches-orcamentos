import { useForm } from 'react-hook-form';
import PanelLayout from '../components/PanelLayout';
import FormTextField from '../components/FormTextField';
import FormSelect from '../components/FormSelect';
import { useState } from 'react';

function CreateBudget() {
    const [ sidebarOpen, setSidebarOpen ] = useState();

    const { control, register, handleSubmit, formState: { errors }} = useForm();

    const [ clients, setClients ] = useState([]);

    const onSubmit = (data) => {
        console.log(data)
    };
    
    const showRequiredErrorMessage = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Campo obrigatório.</p>
    );

    return (
        <div className="bg-slate-200">
            <PanelLayout actualSection={"criar-orcamento"} sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}/>

            <main className="min-h-screen mt-[-15px] flex justify-center items-center">
                <div className='flex flex-col w-[50%] lg:ml-[310px] px-5 py-8 bg-white rounded-lg'>
                    <div className='mb-4'>
                        <h3 className='text-2xl'>
                            Criar Orçamento
                        </h3>
                    </div>

                    <form onSubmit={handleSubmit(onSubmit)} className='space-y-6'>
                        <div>    
                            <FormTextField
                                id={"description"}
                                label={"Descrição"} 
                                placeholder={"Descrição do Orçamento"} 
                                register={register('description', {required: true})} 
                            />
                        </div>

                        {errors?.description?.type === "required" && showRequiredErrorMessage()}

                        <div>
                            <FormSelect 
                                id={"client"}
                                name="client"
                                label={"Cliente"}
                                control={control}
                                options={clients}
                            />
                        </div>

                        <div>
                            <FormTextField 
                                id={"proposalNumber"}
                                label={"Número da Proposta"}
                                placeholder={"Ex: \"2025/0072\""}
                                register={register('proposalNumber', {required: "Campo obrigatório"})}
                            />
                        </div>

                        {errors?.proposalNumber?.type === "required" && showRequiredErrorMessage()}


                        <div className="flex justify-end">
                            <button type="submit" className='px-10 py-2 border border-slate-300 hover:border-slate-400 rounded-lg text-slate-700 outline-none cursor-pointer'>Criar</button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    );
}

export default CreateBudget;