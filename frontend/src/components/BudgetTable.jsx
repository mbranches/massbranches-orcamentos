import { useState } from 'react';
import {createStage, createBudgetItem, deleteItemByBudgetId, deleteStageByBudgetId, editBudgetItem} from '../services/budget';
import { toast } from 'react-toastify';
import statusValidate from '../utils/statusValidate';
import Th from '../components/Th';
import BudgetItemRow from '../components/BudgetItemRow';
import BudgetStageRow from '../components/BudgetStageRow';
import { formatDecimal } from '../utils/format';
import { createItem } from '../services/item';
import NewStage from './NewStage';
import NewItem from './NewItem';

function BudgetTable({elements, newItem, setNewItem, itemToEdit, setItemToEdit, newStage, setNewStage, fetchBudgetElements, setLoading, currentBudget, fetchBudget}) {
    const [newStageErrors, setNewStageErrors] = useState();
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
    const handlerRemoveNewBudgetItem = () => {
        setNewItem(null);
    };
    const handlerRemoveNewStage = () => {
        setNewStage(null);
        setNewStageErrors(null);
    };
    const handlerRemoveEditBudgetItem = () => {
        setItemToEdit(null);
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

    const getStageIdOfItemIfExists = (order) => {
        const [orderIntegerPart, orderDecimalPart] = order.split(".");
        const stageOrder = `${orderIntegerPart}.0`;

        const stage = elements.find(element => element.type === "STAGE" && element.order === stageOrder);

        return stage ? stage.id : null;
    }

    const saveItem = async (data) => {
        const formatedUnitPrice = formatDecimal(data.unitPrice);
        const formatedQuantity = formatDecimal(data.quantity);
        
        setLoading(true);
        
        try {
            let itemId = data.itemId;
            if(!itemId) {
                const response = await createItem(data.name, data.unitMeasurement, formatedUnitPrice);
                itemId = response.data.id;
            }
            
            const stageId = getStageIdOfItemIfExists(data.order);

            await createBudgetItem(currentBudget.id, stageId, data.order, itemId, data.unitMeasurement, formatedUnitPrice, formatedQuantity);

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

    const editItem = async (data) => {
        const formatedUnitPrice = formatDecimal(data.unitPrice);
        const formatedQuantity = formatDecimal(data.quantity);
            
        setLoading(true);
        
        try {
            let itemId = data.itemId;
            if(!itemId) {
                const response = await createItem(data.name, data.unitMeasurement, formatedUnitPrice);
                itemId = response.data.id;
            }
            
            const stageId = getStageIdOfItemIfExists(data.order);

            await editBudgetItem(currentBudget.id, {
                id: itemToEdit.id, 
                stageId, 
                order: data.order, 
                itemId: itemId, 
                unitMeasurement: data.unitMeasurement,
                unitPrice: formatedUnitPrice, 
                quantity: formatedQuantity
            });

            setItemToEdit(null);
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
                                    return <BudgetStageRow key={`stage ${element?.id}`} stage={element} onDeleteButtonClick={() => deleteStage(element?.id)} />;
                                case "ITEM":
                                    return element !== itemToEdit ? (
                                        <BudgetItemRow 
                                            key={`item ${element?.id}`} 
                                            item={element} 
                                            onEditButtonClick={() => setItemToEdit(element)} 
                                            onDeleteButtonClick={() => deleteItem(element?.id)} 
                                        />
                                    ) : (
                                        <NewItem 
                                            key={`item edit ${element?.id}`}
                                            newItem={itemToEdit}
                                            setNewItem={setItemToEdit}
                                            handlerRemove={handlerRemoveEditBudgetItem}
                                            handlerSave={editItem}
                                        />
                                    )
                            }
                        })}

                        <NewStage newStage={newStage} setNewStage={setNewStage} handlerRemove={handlerRemoveNewStage} handlerSave={saveStage} newStageErrors={newStageErrors} />

                        <NewItem 
                            newItem={newItem} 
                            setNewItem={setNewItem} 
                            handlerRemove={handlerRemoveNewBudgetItem} 
                            handlerSave={saveItem} 
                        />

                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default BudgetTable;