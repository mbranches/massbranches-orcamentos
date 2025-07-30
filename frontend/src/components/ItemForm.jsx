import { X } from "lucide-react";
import { useForm } from "react-hook-form";
import FormTextField from "./FormTextField";
import FormSubmitButton from "./FormSubmitButton";
import ErrorMessage from "./ErrorMessage";

function ItemForm({ title, onSubmit, setFormIsOpen, submitButtonLabel, defaultValues = {} }) {
    const { handleSubmit, register, formState: { errors } } = useForm({defaultValues});

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
                    onClick={() => setFormIsOpen(false)}
                >
                    <X size={18}/>
                </div>
            </div>

            <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-5">
                <div>
                    <FormTextField 
                        id={"name"} 
                        placeholder={"Nome do Item"} 
                        label={"Nome"} 
                        register={register("name", {required: "Campo obrigatório."})} 
                        type={"text"} 
                    />
                </div>

                {errors?.name && (<ErrorMessage>{errors?.name?.message}</ErrorMessage> )}


                <div className="flex flex-col sm:flex-row gap-3">
                    <div className="flex flex-col gap-3">
                        <FormTextField 
                            id={"unitMeasurement"} 
                            placeholder={"Unidade de medida"} 
                            label={"Unidade"} 
                            register={register("unitMeasurement", {required: "Campo obrigatório."})} 
                            type={"text"} 
                        />
                        
                        {errors?.unitMeasurement && (<ErrorMessage>{errors?.unitMeasurement?.message}</ErrorMessage> )}
                    </div>
                    

                    <div className="flex flex-col gap-3">
                        <FormTextField 
                            id={"unitPrice"} 
                            placeholder={"Ex: 259,98"} 
                            label={"Preço unitário"} 
                            register={register(
                                "unitPrice", {
                                    required: "Campo obrigatório.", 
                                    pattern: {
                                        value: /^\d+([.,]\d{1,4})?$/, message: "Digite um valor válido"
                                    }}
                                )
                            } 
                            type={"text"} 
                        />

                        {errors?.unitPrice && (<ErrorMessage>{errors?.unitPrice?.message}</ErrorMessage>)}
                    </div>

                </div>

                {errors?.type && (<ErrorMessage>{errors?.type?.message}</ErrorMessage>)}

                <div className="flex justify-end">
                    <FormSubmitButton label={submitButtonLabel}/>
                </div>
            </form>
        </div>
    );
}

export default ItemForm;