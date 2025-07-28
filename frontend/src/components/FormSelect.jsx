import { Controller } from "react-hook-form";
import FormLabel from "./FormLabel";
import Select from "react-select";

function FormSelect({ id, name, label, control, options, noOptionsMessage, placeholder, isSearchable=false, rules={} }) {
    const mappedOptions = options.map((opt) => ({
        value: opt.id,
        label: opt.name
    }));

    const customStyles = {
        control: (base, state) => ({
          ...base,
          boxShadow: "none",
          borderColor: state.isFocused ? "#90a1b9" : "#cad5e2",
          "&:hover": {
            borderColor: "#cad5e2",
          },
          cursor: "pointer"
        })
      };
      

    return (
        <div>
            <div className="mb-2">
                <FormLabel idToBeReferenced={id}>
                    {label}
                </FormLabel>
            </div>
            
            <Controller 
                name={name}
                control={control}
                rules={rules}
                render={({ field }) => (
                    <Select 
                        {...field}
                        inputId={id}
                        options={mappedOptions}
                        placeholder={placeholder}
                        styles={customStyles}
                        isSearchable={isSearchable}
                        noOptionsMessage={() => noOptionsMessage}
                    />
                )}  
            />
        </div>
    );
}

export default FormSelect;