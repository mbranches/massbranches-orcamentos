import { Controller, useForm } from "react-hook-form";
import BudgetItem from "./BudgetItem";
import BudgetItemData from "./BudgetItemData";
import ElementDataInput from "./ElementDataInput";
import NewElementActions from "./NewElementActions";
import NewItemNameInput from "./NewItemNameInput";
import { useEffect } from "react";

function NewItem({ newItem, handlerRemove, handlerSave }) {
    const {
        register,
        handleSubmit,
        setValue,
        watch,
        reset,
        control,
        formState: { errors },
    } = useForm({
        defaultValues: newItem || {},
    });
    
    useEffect(() => {
        if (newItem) {
            reset(newItem);
        }
    }, [newItem, reset]);


    const onSubmit = (data) => {
        handlerSave(data);
    };

    return ( newItem && (
        <BudgetItem>
            <BudgetItemData>
                <ElementDataInput
                    {...register("order", {
                        required: "Campo obrigatório",
                        pattern: {
                        value: /^\d+\.\d+$/,
                        message: "Formato inválido. Ex: 1.0",
                        },
                    })}
                    placeholder={"Ord."}
                    throwableError={errors.order?.message}
                />
            </BudgetItemData>

            <BudgetItemData>
                <Controller
                    rules={{required: "Campo obrigatório"}}
                    control={control}
                    name="name"
                    render={ ({field}) => (
                            <NewItemNameInput
                                {...field}
                                placeholder={"Nome do item"}
                                setValue={setValue}
                                throwableError={errors.name?.message}
                                watch={watch}
                            />
                        )
                    }
                />
            </BudgetItemData>

            <BudgetItemData>
                <ElementDataInput
                    {...register("unitMeasurement", {
                        required: "Campo obrigatório",
                    })}
                    placeholder="Unidade"
                    throwableError={errors.unitMeasurement?.message}
                />
            </BudgetItemData>

            <BudgetItemData>
                <ElementDataInput
                    {...register("unitPrice", {
                        required: "Campo obrigatório",
                        pattern: {
                        value: /^\d+(\.\d+)?$/,
                        message: "Formato inválido. Ex: 1.00",
                        },
                    })}
                    placeholder="Valor Unitário"
                    throwableError={errors.unitPrice?.message}
                />
            </BudgetItemData>

            <BudgetItemData>
                <ElementDataInput
                    {...register("quantity", {
                        required: "Campo obrigatório",
                        pattern: {
                        value: /^\d+(\.\d+)?$/,
                        message: "Formato inválido. Ex: 1.00",
                        },
                    })}
                    placeholder="Qtd."
                    throwableError={errors.quantity?.message}
                />
            </BudgetItemData>

            <td colSpan={1}></td>

            <BudgetItemData>
                <NewElementActions
                    handlerSave={handleSubmit(onSubmit)}
                    handlerRemove={handlerRemove}
                />
            </BudgetItemData>
        </BudgetItem>
    ));
}

export default NewItem
