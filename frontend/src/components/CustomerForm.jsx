import { useForm } from "react-hook-form";
import FormSelect from "./FormSelect";
import FormTextField from "./FormTextField";
import FormSubmitButton from '../components/FormSubmitButton';
import { X } from "lucide-react";
import ErrorMessage from '../components/ErrorMessage';

function CustomerForm({ onSubmit, submitButtonLabel, setFormOpen, title, defaultValues }) {
    const customerTypeSelectOptions = [
        {
            id: "PESSOA_FISICA",
            name: "Pessoa física"
        },
        {
            id: "PESSOA_JURIDICA",
            name: "Pessoa jurídica"
        },
    ];
    
    const { handleSubmit, register, control, formState: { errors } } = useForm(
        {
            defaultValues: defaultValues ? {...defaultValues, type: {
                value: defaultValues.type, 
                label: customerTypeSelectOptions.find(ct => ct.id === defaultValues.type).name
            }} : null
        }
    );
    
    return (
            <div 
                className='flex flex-col gap-4' 
            >
                <div className="flex justify-between items-center">
                    <h3 className="text-2xl font-bold">
                        {title}
                    </h3>

                    <div 
                        className="hover:bg-gray-100 p-1 rounded-md cursor-pointer"
                        onClick={() => setFormOpen(false)}
                    >
                        <X size={18}/>
                    </div>
                </div>

                <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-5">
                    <div>
                        <FormTextField 
                            id={"name"} 
                            placeholder={"Nome do cliente"} 
                            label={"Nome"} 
                            register={register("name", {required: "Campo obrigatório."})} 
                            type={"text"} />
                    </div>

                    {errors?.name && (<ErrorMessage>{errors?.name?.message}</ErrorMessage> )}


                    <div>
                        <FormSelect 
                            id={"type"} 
                            name={"type"}
                            noOptionsMessage={"Nenhum tipo encontrado"} 
                            options={customerTypeSelectOptions} 
                            control={control} 
                            label={"Tipo do cliente"}
                            placeholder={"Selecione o tipo do cliente"}
                            rules={{required: "Campo obrigatório"}}
                        />
                    </div>

                    {errors?.type && (<ErrorMessage>{errors?.type?.message}</ErrorMessage> )}

                    <div className="flex justify-end">
                        <FormSubmitButton label={submitButtonLabel}/>
                    </div>
                </form>
            </div>
    );
}

export default CustomerForm;