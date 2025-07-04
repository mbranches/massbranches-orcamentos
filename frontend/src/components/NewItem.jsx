import BudgetItem from "./BudgetItem";
import BudgetItemData from "./BudgetItemData";
import ElementDataInput from "./ElementDataInput";
import NewElementActions from "./NewElementActions";
import NewItemNameInput from "./NewItemNameInput";

function NewItem({newItem, setNewItem, newItemErrors, handlerRemove, handlerSave}) {
    return (
        newItem && (
            <BudgetItem>
                <BudgetItemData>
                    <ElementDataInput
                        value={newItem.order}
                        onChange={(e) => setNewItem({ ...newItem, order: e.target.value })}
                        throwableError={newItemErrors?.order}
                    />
                </BudgetItemData>
                <BudgetItemData>
                    <NewItemNameInput
                        value={newItem.item.name}
                        placeholder="Nome do Item"
                        setNewItem={setNewItem}
                        newItem={newItem}
                        throwableError={newItemErrors?.name}
                    />
                </BudgetItemData>
                <BudgetItemData>
                    <ElementDataInput
                        value={newItem.item.unitMeasurement}
                        onChange={(e) => setNewItem({ ...newItem, item: {...newItem.item, unitMeasurement: e.target.value} })}
                        placeholder="Unidade"
                        disabled={!!newItem.item.id}
                        throwableError={newItemErrors?.unitMeasurement}
                    />
                </BudgetItemData>
                <BudgetItemData>
                    <ElementDataInput
                        value={newItem.unitPrice}
                        onChange={(e) => setNewItem({ ...newItem, unitPrice: e.target.value })}
                        placeholder="Valor Unitário"
                        throwableError={newItemErrors?.unitPrice}
                    />
                </BudgetItemData>
                <BudgetItemData>
                    <ElementDataInput
                        value={newItem.quantity}
                        onChange={(e) => setNewItem({ ...newItem, quantity: e.target.value })}
                        placeholder="Qtd."
                        throwableError={newItemErrors?.quantity}
                    />
                </BudgetItemData>
                <td colSpan={1}></td>
                <BudgetItemData>
                    <NewElementActions handlerSave={handlerSave} handlerRemove={handlerRemove} />
                </BudgetItemData>
            </BudgetItem>
        )
    );
}

export default NewItem;