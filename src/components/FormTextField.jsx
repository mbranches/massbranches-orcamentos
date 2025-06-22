import FormLabel from "./FormLabel";

function FormTextField({ id, label, placeholder, register }) {
    return (
        <div>
            <div className="mb-2">
                <FormLabel idToBeReferenced={id}>
                    {label}
                </FormLabel>
            </div>
            
            <input 
                type="text" 
                id={id} 
                className='w-full border border-slate-300 focus:border-slate-400 rounded-md px-4 py-2 outline-none' 
                {...register} 
                placeholder={placeholder} 
            />
        </div>
    );
}

export default FormTextField;