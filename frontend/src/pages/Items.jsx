import { useEffect, useState } from "react";
import PanelLayout from "../components/PanelLayout";
import ButtonNew from "../components/ButtonNew";
import { createItem, deleteItemById, listMyAllItems, searchItemsByName, updateItem } from "../services/item";
import LoadingScreen from "../components/LoadingScreen";
import SearchBar from "../components/SearchBar";
import ItemCard from "../components/ItemCard";
import { toast } from "react-toastify";
import statusValidate from '../utils/statusValidate';
import ItemForm from "../components/ItemForm";
import Modal from "../components/Modal";
import DeleteConfirmationBox from "../components/DeleteConfirmationBox";
import { formatDecimal } from "../utils/format";
import ItemDetails from "../components/ItemDetails";

function Items() {
    const [sidebarOpen, setSidebarOpen] = useState();

    const [items, setItems] = useState([]);
    
    const [loading, setLoading] = useState(false);

    const [createNewItemModalIsOpen, setCreateNewItemModalIsOpen] = useState(false);
    
    const [updateItemModalIsOpen, setUpdateItemModalIsOpen] = useState(false);
    
    const [deleteItemModalIsOpen, setDeleteItemModalIsOpen] = useState(false);
    
    const [itemDetailsModalIsOpen, setItemDetailsModalIsOpen] = useState(false);

    const [itemToUpdate, setItemToUpdate] = useState(null);

    const [itemToDelete, setItemToDelete] = useState(null);

    const [itemToViewDetails, setItemToViewDetails] = useState(null);

    const [refreshSidebar, setRefreshSidebar] = useState(0);

    const [nameToSearch, setNameToSearch] = useState();

    const fetchItems = async () => {
        setLoading(true);

        try {
            const response = await listMyAllItems();

            setItems(response.data);
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente");
                
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const filterItems = async (name) => {
        setNameToSearch(name);

        if(!name || name.trim() === "") {
            fetchItems();
            return;    
        }

        try {
            const response = await searchItemsByName(name);

            setItems(response.data);
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente");
                
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const onSumbitCreateItemForm = async (data) => {
        setLoading(true);

        try{
            await createItem(data.name, data.unitMeasurement, formatDecimal(data.unitPrice));

            toast.success("Item criado com sucesso");

            setCreateNewItemModalIsOpen(false)
            setRefreshSidebar(refreshSidebar + 1);
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
            
            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };
    
    const onSumbitUpdateItemForm = async (data) => {
        setLoading(true);

        try{
            await updateItem({
                id: itemToUpdate.id,
                name: data.name,
                unitMeasurement: data.unitMeasurement,
                unitPrice: formatDecimal(data.unitPrice),
            });
            
            toast.success("Item atualizado com sucesso");
            setRefreshSidebar(refreshSidebar + 1);

            setUpdateItemModalIsOpen(false)
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 

            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const deleteItem = async () => {
        setLoading(true);

        try{
            await deleteItemById(itemToDelete.id);
            
            toast.success("Item deletado com sucesso");
            setRefreshSidebar(refreshSidebar + 1);

            setDeleteItemModalIsOpen(false)
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 

            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const onEditButtonClick = (item) => {
        setUpdateItemModalIsOpen(true);
        setItemToUpdate(item)
    };

    const onDeleteButtonClick = (item) => {
        setDeleteItemModalIsOpen(true);
        setItemToDelete(item);
    };

    const onViewButtonClick = (item) => {
        setItemDetailsModalIsOpen(true);
        setItemToViewDetails(item);
    };
    
    useEffect(() => {
        if(nameToSearch) {
            filterItems(nameToSearch);
            return;
        }

        fetchItems();
    }, [refreshSidebar])

    return (
        <PanelLayout actualSection={"visualizar-itens"} sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}>
            {loading && <LoadingScreen />}

            <div className='w-full min-h-screen mt lg:ml-[310px] mt-[83px] p-4 flex flex-col gap-4'>
                <div className='flex flex-col gap-4 sm:flex-row justify-between sm:items-center bg-white'>
                    <div>
                        <h2 className='text-3xl font-bold mb-1'>Itens</h2>
                        <p className='text-gray-600'>{items.length} itens encontrados.</p>
                    </div>
        
                    <div>
                        <ButtonNew label={'Novo Item'} onClick={() => setCreateNewItemModalIsOpen(true)} />
                    </div>
                </div>

                <div>
                    <SearchBar placeholder={"Buscar item pelo nome"} onSearch={(e) => {filterItems(e.target.value)}}/>
                </div>

                <div className="flex flex-col gap-3">
                    {items.map(item =>
                        <ItemCard key={item.id} 
                            item={item} 
                            onEditButtonClick={() => onEditButtonClick(item)} 
                            onDeleteButtonClick={() => onDeleteButtonClick(item)}
                            onViewButtonClick={() => onViewButtonClick(item)}
                        />
                    )}
                </div>
            </div>

            {createNewItemModalIsOpen && (
                <Modal modalIsOpen={createNewItemModalIsOpen} setModalIsOpen={setCreateNewItemModalIsOpen} className={"w-2/3 md:w-1/3"}>
                    <ItemForm 
                        title={"Criar novo item"} 
                        onSubmit={onSumbitCreateItemForm} 
                        setFormIsOpen={setCreateNewItemModalIsOpen} 
                        submitButtonLabel={"Criar"} 
                    />
                </Modal>
            )}

            {updateItemModalIsOpen && (
                <Modal modalIsOpen={updateItemModalIsOpen} setModalIsOpen={setUpdateItemModalIsOpen} className={"w-2/3 md:w-1/3"}>
                    <ItemForm 
                        title={"Atualizar item"} 
                        onSubmit={onSumbitUpdateItemForm}
                        setFormIsOpen={setUpdateItemModalIsOpen} 
                        submitButtonLabel={"Atualizar"} 
                        defaultValues={itemToUpdate} 
                    />
                </Modal>
            )}

            {deleteItemModalIsOpen && (
                <DeleteConfirmationBox 
                    deleteConfirmationBoxIsOpen={deleteItemModalIsOpen} 
                    setDeleteConfirmationBoxIsOpen={setDeleteItemModalIsOpen} 
                    objectToDelete={itemToDelete?.name} 
                    onDelete={deleteItem} 
                />
            )}

            {itemDetailsModalIsOpen && (
                <Modal modalIsOpen={itemDetailsModalIsOpen} setModalIsOpen={setItemDetailsModalIsOpen} className={"w-2/3 md:w-1/3"}>
                    <ItemDetails 
                        setModalIsOpen={setItemDetailsModalIsOpen}
                        item={itemToViewDetails}
                    />
                </Modal>
            )}
        </PanelLayout>
    );
}

export default Items;