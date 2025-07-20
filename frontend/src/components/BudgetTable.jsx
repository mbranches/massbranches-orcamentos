import { useState } from 'react';
import {createStage, createBudgetItem, deleteItemByBudgetId, deleteStageByBudgetId} from '../services/budget';
import { toast } from 'react-toastify';
import statusValidate from '../Utils/statusValidate';
import Th from '../components/Th';
import BudgetItemRow from '../components/BudgetItemRow';
import BudgetStageRow from '../components/BudgetStageRow';
import { formatDecimal } from '../utils/format';
import { createItem } from '../services/item';
import NewStage from './NewStage';
import NewItem from './NewItem';

function BudgetTable({elements, newItem, setNewItem, newStage, setNewStage, fetchBudgetElements, setLoading, currentBudget, fetchBudget}) {
    const [newStageErrors, setNewStageErrors] = useState();
    const [newItemErrors, setNewItemErrors] = useState();
    const validateNewStage = () => {
        const errors = {};

        const requiredField = "Campo obrigatório.";

        if (!newStage.order.trim()) {
            errors.order = requiredField;
        } else if (!/^\d+\.\d+$/.test(newStage.order)) {
            errors.order = "Formato inválido. Ex: 1.0";
        }

        if(!newStage.name.trim()) {
            errors.name = requiredField;
        }

        setNewStageErrors(errors);

        return Object.keys(errors).length === 0;
    };
    const validateNewItem = () => {
        const errors = {};

        const requiredField = "Campo obrigatório.";

        const invalidDecimalNumber = "Formato inválido. Ex: 1.0";

        const invalidNumeric = "Formato inválido. Ex: 1 ou 1.00";

        if (!newItem.order.trim()) {
            errors.order = requiredField;
        } else if (!/^\d+\.\d+$/.test(newItem.order)) {
            errors.order = invalidDecimalNumber;
        }

        if(!newItem.item.name.trim()) {
            errors.name = requiredField;
        }

        if(!newItem.item.unitMeasurement.trim()) {
            errors.unitMeasurement = requiredField;
        }

        if(!newItem.unitPrice) {
            errors.unitPrice = requiredField;
        } else if (isNumeric(newItem.unitPrice)) {
            errors.unitPrice = invalidNumeric;
        }

        if(!newItem.quantity.trim()) {
            errors.quantity = requiredField;
        } else if (isNumeric(newItem.quantity)) {
            errors.quantity = invalidNumeric;
        }

        setNewItemErrors(errors);

        return Object.keys(errors).length === 0;
    };
    const isNumeric = (input) => {
        const numericRegex = /!^\d+\.\d+$/
        
        return numericRegex.test(input);
    };
    const handlerRemoveNewBudgetItem = () => {
        setNewItem(null);
        setNewItemErrors(null);
    };
    const handlerRemoveNewStage = () => {
        setNewStage(null);
        setNewStageErrors(null);
    };

    const deleteItem = async (itemId) => {
        setLoading(true);

        try {
            await deleteItemByBudgetId(currentBudget.id, itemId);

            await fetchBudgetElements();
            await fetchBudget();
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
            statusValidate(status);

            if(status === 404 || status === 400) {
                toast.error("Ocorreu um erro interno, por favor tente novamente");
            }

        } finally {
            setLoading(false);
        }
    };

    const deleteStage = async (stageId) => {
        setLoading(true);

        try {
            await deleteStageByBudgetId(currentBudget.id, stageId);

            await fetchBudgetElements();
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
            statusValidate(status);

            if(status === 404 || status === 400) {
                toast.error("Ocorreu um erro interno, por favor tente novamente");
            }

        } finally {
            setLoading(false);
        }
    };

    const saveStage = async () => {
        if(!validateNewStage()) return;

        setLoading(true);

        try {
            await createStage(currentBudget.id, newStage.order, newStage.name);

            setNewStage(null);

            await fetchBudgetElements();      
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const saveItem = async () => {
        if(!validateNewItem()) return;
        
        const formatedUnitPrice = formatDecimal(newItem.unitPrice);
        const formatedQuantity = formatDecimal(newItem.quantity);
        
        setLoading(true);
        
        try {
            let item;
            if(!newItem.item.id) {
                const response = await createItem(newItem.item.name, newItem.item.unitMeasurement, formatedUnitPrice)
                
                item = response.data;
            } else item = newItem.item;
            
            await createBudgetItem(currentBudget.id, newItem.order, item.id, formatedUnitPrice, formatedQuantity);

            setNewItem(null);

            await fetchBudgetElements();     
            await fetchBudget(); 
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className='bg-white'>
            <div className='p-6'>
                <h3 className='font-bold text-2xl'>
                    Itens do Orçamento
                </h3>
            </div>
            <div className='overflow-x-auto'>
                <table className='w-full min-w-[900px] max-w-[1200px] text-[12px]'>
                    <thead className='bg-gray-50 border-b border-gray-200'>
                        <tr>
                            <Th>Ordem</Th>
                            <Th width={"w-1/2"}>Nome do Item</Th>
                            <Th width={"w-[11%]"}>Unidade</Th>
                            <Th width={"w-[15%]"}>Valor unit.</Th>
                            <Th width={"w-[10%]"}>Qtd.</Th>
                            <Th width={"w-[15%]"}>Total</Th>
                            <Th width={"w-[10%]"}>Ações</Th>
                        </tr>
                    </thead>
                    <tbody>
                        {elements.map(element => {
                            switch(element.type) {
                                case "STAGE":
                                    return <BudgetStageRow key={element?.id} stage={element} onDeleteButtonClick={() => deleteStage(element?.id)} />;
                                case "ITEM":
                                    return <BudgetItemRow key={element?.id} item={element} onDeleteButtonClick={() => deleteItem(element?.id)} />;
                            }
                        })}

                        <NewStage newStage={newStage} setNewStage={setNewStage} handlerRemove={handlerRemoveNewStage} handlerSave={saveStage} newStageErrors={newStageErrors} />

                        <NewItem newItem={newItem} setNewItem={setNewItem} handlerRemove={handlerRemoveNewBudgetItem} handlerSave={saveItem} newItemErrors={newItemErrors} />

                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default BudgetTable;