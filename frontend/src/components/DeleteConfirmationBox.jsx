function DeleteConfirmationBox({ deleteConfirmationBoxIsOpen, setDeleteConfirmationBoxIsOpen, onDelete, objectToDelete }) {
        return deleteConfirmationBoxIsOpen && (
            <div 
                className='fixed inset-0 bg-black/10 flex justify-center items-center'
                onClick={() => setDeleteConfirmationBoxIsOpen(false)}
            >
                <div 
                    className='lg:ml-[310px] flex justify-center items-center flex-col gap-4 bg-white p-5 rounded-md shadow-lg'
                    onClick={(e) => e.stopPropagation()}
                >
                    <div>
                        <h3>{`Excluir "${objectToDelete}" ?`}</h3>
                        <p className='text-sm text-gray-600'>Essa ação não poderá ser desfeita</p>
                    </div>

                    <div className='flex gap-3'>
                        <button 
                            onClick={() => setDeleteConfirmationBoxIsOpen(false)}
                            className='border border-gray-200 text-sm px-4 py-2 rounded-md hover:border-gray-300 transition-all duration-100 cursor-pointer'
                        >
                            Cancelar
                        </button>

                        <button 
                            onClick={onDelete}
                            className='bg-red-500 text-white text-sm px-4 py-2 rounded-md hover:bg-red-600 transition-all duration-100 cursor-pointer'
                        >
                            Confirmar
                        </button>
                    </div>
                </div>
            </div>
        );
}

export default DeleteConfirmationBox;