namespace ASP.NET_MVC.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("Medicamento")]
    public partial class Medicamento
    {
        public Medicamento()
        {
            DetalleBoleta = new HashSet<DetalleBoleta>();
        }

        public int MedicamentoId { get; set; }

        [Required]
        [StringLength(100)]
        public string Nombre { get; set; }

        public int EspecieId { get; set; }

        public int TipoMedicamentoId { get; set; }

        public int LaboratorioId { get; set; }

        [StringLength(300)]
        public string Descripcion { get; set; }

        [DataType(DataType.Currency)]
        [DisplayFormat(DataFormatString = "{0:C2}", ApplyFormatInEditMode = false)]
        public decimal Precio { get; set; }

        public int Stock { get; set; }

        public byte Estado { get; set; }
        
        [NotMapped]
        public string MedicamentoEspecie { get { return string.Format("{0} - {1} - {2}", Nombre, Especie.Descripcion, Stock); } }

        public virtual ICollection<DetalleBoleta> DetalleBoleta { get; set; }

        public virtual Especie Especie { get; set; }

        public virtual Laboratorio Laboratorio { get; set; }

        public virtual TipoMedicamento TipoMedicamento { get; set; }
    }
}
