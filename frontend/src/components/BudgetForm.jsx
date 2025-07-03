import { useForm } from "react-hook-form";
import FormSelect from "./FormSelect";
import FormTextField from "./FormTextField";

function BudgetForm({ onSubmit, customers, defaultValues={} }) {
    const showRequiredErrorMessage = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Campo obrigatório.</p>
    );
    const showBdiInvalidError = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Digite um BDI válido.</p>
    );
    const { control, register, handleSubmit, formState: { errors }} = useForm({defaultValues});
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
    );
}

export default BudgetForm;