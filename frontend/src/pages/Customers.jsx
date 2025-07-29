import { useEffect, useState } from 'react';
import PanelLayout from '../components/PanelLayout';
import LoadingScreen from '../components/LoadingScreen';
import ButtonNew from '../components/ButtonNew';
import { createCustomer, deleteCustomerById, filterMyAllCustomers, listMyAllCustomers, updateCustomer } from '../services/customer';
import { toast } from 'react-toastify';
import SearchBar from '../components/SearchBar'
import FilterSelect from '../components/FilterSelect'
import CustomerCard from '../components/CustomerCard';
import DeleteConfirmationBox from '../components/DeleteConfirmationBox';
import statusValidate from '../utils/statusValidate';
import CustomerForm from '../components/CustomerForm'
import CustomerDetails from '../components/CustomerDetails';
import Modal from '../components/Modal';

function Customers() {
    const [customers, setCustomers] = useState([]);

    const [loading, setLoading] = useState(false);

    const [sidebarOpen, setSidebarOpen] = useState(false);

    const [refreshSidebar, setRefreshSidebar] = useState(0);

    const [customerToDelete, setCustomerToDelete] = useState(null);
    
    const [customerToUpdate, setCustomerToUpdate] = useState(null);

    const [customerToView, setCustomerToView] = useState(null);
    
    const [deleteConfirmationBoxIsOpen, setDeleteConfirmationBoxIsOpen] = useState(false);

    const [createNewCustomerFormIsOpen, setCreateNewCustomerFormIsOpen] = useState(false);

    const [updateCustomerFormIsOpen, setUpdateCustomerFormIsOpen] = useState(false);

    const [customerDetailsBoxIsOpen, setCustomerDetailsBoxIsOpen] = useState(false);

    const [nameToSearch, setNameToSearch] = useState(null);

    const [typeToSearch, setTypeToSearch] = useState(null);

    const fetchCustomers = async () => {
        setLoading(true);

        try{
            const response = await listMyAllCustomers();

            setCustomers(response);
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente");

            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const customerTypeFilterOptions = [
        { value: "all", label: "Todos" },
        { value: "PESSOA_FISICA", label: "Pessoa física" },
        { value: "PESSOA_JURIDICA", label: "Pessoa jurídica" },
    ]

    const deleteCustomer = async (customer) => {
        setLoading(true);

        try {
            await deleteCustomerById(customer.id);

            setRefreshSidebar(refreshSidebar + 1);

            setDeleteConfirmationBoxIsOpen(false);
            setCustomerToDelete(null);

            toast.success("Cliente deletado com sucesso")
        } catch(error) {
            const status = error?.response?.status || 
                
            statusValidate(status);
        } finally {
            setLoading(false)
        }
    };

    const onSumbitCreateCustomerForm = async (data) => {
        setLoading(true);

        try{
            await createCustomer({name: data.name, type: data.type.value});

            toast.success("Cliente criado com sucesso");

            setCreateNewCustomerFormIsOpen(false)
            setRefreshSidebar(refreshSidebar + 1);
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 

            statusValidate(status);
        } finally {
            setLoading(false);
        }
    };

    const onSumbitUpdateCustomerForm = async (data) => {
        setLoading(true);
        try {
            await updateCustomer({id: customerToUpdate.id, name: data.name, type: data.type.value});

            setRefreshSidebar(refreshSidebar + 1);
            setCustomerToUpdate(null);
            setUpdateCustomerFormIsOpen(false);
        } catch(error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 

            statusValidate(status);
        } finally {
            setLoading(false);
        }

    };

    const onDeleteCustomerButtonClick = (customer) => {
        setDeleteConfirmationBoxIsOpen(true);
        setCustomerToDelete(customer);
    };

    const onUpdateCustomerButtonClick = (customer) => {
        setUpdateCustomerFormIsOpen(true);    
        setCustomerToUpdate(customer)
    };

    const onViewButtonClick = (customer) => {
        setCustomerDetailsBoxIsOpen(true);
        setCustomerToView(customer);
    };

    const filterCustomer = async (name, type) => {
        setNameToSearch(name);
        setTypeToSearch(type);

        if(name?.trim() === "" && (type === "all" || !type)) {
            fetchCustomers();
            return;
        }

        try {
            const response = await filterMyAllCustomers(
                name?.trim() || null, 
                type === "all" ? null : type
            );

            setCustomers(response.data);

        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente");

            statusValidate(status);
        }
    };

    useEffect(() => {
        if(nameToSearch || typeToSearch) {
            filterCustomer(nameToSearch, typeToSearch);
            return;
        }

        fetchCustomers();
    }, [refreshSidebar])

    return (
        <PanelLayout actualSection={"visualizar-clientes"} setSidebarOpen={setSidebarOpen} sidebarOpen={sidebarOpen} refreshSidebar={refreshSidebar}>
            {loading && <LoadingScreen />}

            <div className='w-full min-h-screen mt lg:ml-[310px] mt-[83px] p-4 flex flex-col gap-4'>
                <div className='flex flex-col gap-4 sm:flex-row justify-between sm:items-center bg-white'>
                    <div>
                        <h2 className='text-3xl font-bold mb-1'>Clientes</h2>
                        <p className='text-gray-600'>{customers.length} clientes encontrados.</p>
                    </div>
        
                    <div>
                        <ButtonNew label={'Novo Cliente'} onClick={() => setCreateNewCustomerFormIsOpen(true)} />
                    </div>
                </div>

                <div className='flex flex-col sm:flex-row gap-3'>
                    <div className='w-full sm:w-4/5'>
                        <SearchBar 
                            placeholder={"Buscar por nome do cliente"} 
                            onSearch={e => filterCustomer(e.target.value, typeToSearch)} 
                        />
                    </div>

                    <div className='w-full sm:w-1/5'>
                        <FilterSelect 
                            options={customerTypeFilterOptions} 
                            onChange={(e) => filterCustomer(nameToSearch, e.target.value)} 
                            selected={customerTypeFilterOptions[0]} 
                        />
                    </div>
                </div>

                <div className='flex flex-col gap-4 mt-4'>
                    {customers.length > 0 ? customers.map(customer =>
                        <CustomerCard 
                            customer={customer} key={customer.id} 
                            onViewButtonClick={() => onViewButtonClick(customer)} 
                            onEditButtonClick={() => onUpdateCustomerButtonClick(customer)} 
                            onDeleteButtonClick={() => onDeleteCustomerButtonClick(customer)} 
                        />
                    ) : <div className='flex justify-center items-center h-96'>
                            <p className='text-gray-600'>Nenhum cliente encontrado</p>
                        </div>}
                </div>
            </div>

            { createNewCustomerFormIsOpen && (
                <Modal modalIsOpen={createNewCustomerFormIsOpen} setModalIsOpen={setCreateNewCustomerFormIsOpen} className={"w-2/3 md:w-1/3"}>
                    <CustomerForm 
                        onSubmit={onSumbitCreateCustomerForm} 
                        submitButtonLabel={"Criar"} 
                        setFormOpen={setCreateNewCustomerFormIsOpen}
                        title={"Criar novo cliente"}
                    />
                </Modal>
            ) }

            { updateCustomerFormIsOpen && (
                <Modal modalIsOpen={updateCustomerFormIsOpen} setModalIsOpen={setUpdateCustomerFormIsOpen} className={"w-2/3 md:w-1/3"}>
                    <CustomerForm 
                        onSubmit={onSumbitUpdateCustomerForm} 
                        submitButtonLabel={"Atualizar"} 
                        setFormOpen={setUpdateCustomerFormIsOpen}
                        defaultValues={customerToUpdate}
                        title={"Atualizar cliente"}
                    />
                </Modal>
            ) }

            { customerDetailsBoxIsOpen && (
                <Modal modalIsOpen={customerDetailsBoxIsOpen} setModalIsOpen={setCustomerDetailsBoxIsOpen} className='w-11/12 md:w-1/2'>
                    <CustomerDetails 
                        setModalIsOpen={setCustomerDetailsBoxIsOpen}
                        customer={customerToView}
                    />
                </Modal>
            ) }

            <DeleteConfirmationBox 
                onDelete={() => deleteCustomer(customerToDelete)} 
                objectToDelete={customerToDelete?.name} 
                setDeleteConfirmationBoxIsOpen={setDeleteConfirmationBoxIsOpen} 
                deleteConfirmationBoxIsOpen={deleteConfirmationBoxIsOpen} 
            />

        </PanelLayout>
    );         
        
}

export default Customers;