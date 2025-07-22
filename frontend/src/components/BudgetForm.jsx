import { useForm } from "react-hook-form";
import FormSelect from "./FormSelect";
import FormTextField from "./FormTextField";
import { useEffect, useState } from "react";
import {listAllCustomers} from "../services/customer";
import statusValidate from "../Utils/statusValidate";
import { useAuth } from "../hooks/useAuth";
import { toast } from "react-toastify";

function BudgetForm({ submitButtonLabel, onSubmit, defaultValues={}, setLoading, selectedCustomer }) {
    const [ customers, setCustomers ] = useState([]);
    const { isAdmin } = useAuth();

    const showRequiredErrorMessage = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Campo obrigatório.</p>
    );

    const showBdiInvalidError = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Digite um BDI válido.</p>
    );
    
    const { control, register, handleSubmit, formState: { errors }, reset} = useForm({
        defaultValues: {...defaultValues, customer: null, status: { value: "EM_ANDAMENTO", label: "Em andamento" }},
    });

    const budgetStatusOptions = [
        { id: "EM_ANDAMENTO", name: "Em andamento" },
        { id: "EM_ANALISE", name: "Em análise" },
        { id: "APROVADO", name: "Aprovado" },
        { id: "REJEITADO", name: "Rejeitado" },
    ];

    useEffect(() => {
        
        const fetchCustomer = async () => {
            setLoading(true);

            try {
                const customers = isAdmin? await listAllCustomers(true) : await listAllCustomers();

                setCustomers(customers);
            } catch(error) {
                const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
                statusValidate(status);
            } finally {
                setLoading(false);
            }
        };

        fetchCustomer();
    }, []);
    
    useEffect(() => {
        if (selectedCustomer) {
            reset(prev => ({
            ...prev,
            ...defaultValues,
            customer: {
                value: selectedCustomer.id,
                label: selectedCustomer.name
            }
            }));
        }
        }, [selectedCustomer, defaultValues, reset]);

    return(
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
                    noOptionsMessage={"Nenhum cliente encontrado"}
                    placeholder={"Opcional"}
                    isSearchable={true}
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
                <FormSelect 
                    id={"status"}
                    name="status"
                    label={"Status"}
                    control={control}
                    options={budgetStatusOptions}
                    noOptionsMessage={"Nenhum status encontrado"}
                    defaultValue={{ id: "EM_ANDAMENTO", name: "Em andamento" }}
                />
            </div>

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

            {(errors?.bdi?.type === "required" && showRequiredErrorMessage()) || (errors?.bdi?.type === "pattern" && showBdiInvalidError())}

            <div className="flex justify-end">
                <button type="submit" className='w-full py-4 px-10 md:py-2 md:w-auto border border-slate-300 hover:border-slate-400 rounded-lg text-slate-700 outline-none cursor-pointer'>{submitButtonLabel   }</button>
            </div>
        </form>
    );
}

export default BudgetForm;