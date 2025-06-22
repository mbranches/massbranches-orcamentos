function FormLabel({idToBeReferenced, children}) {
    return (
        <label 
            htmlFor={idToBeReferenced} 
            className='block text-slate-700'
        >
            {children}
        </label>
    );
}

export default FormLabel;