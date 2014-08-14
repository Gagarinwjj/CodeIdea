#ASP.NET 验证码

``C#
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.IO;
using System.Drawing.Imaging;

namespace Wedo.BusInfo.WebUI.Controls
{
    public partial class CheckCode : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            CreateValidateCodeImage(GenerateValidateCode());
        }
        #region 生成图片

        private void CreateValidateCodeImage(string ValidateCode)
        {

            Bitmap image = new Bitmap((int)Math.Ceiling((double)(ValidateCode.Length * 12.5)), 0x16);
            Graphics graphics = Graphics.FromImage(image);
            try
            {
                Random random = new Random();
                graphics.Clear(Color.White);
                for (int i = 0; i < 2; i++)
                {
                    int num2 = random.Next(image.Width);
                    int num3 = random.Next(image.Width);
                    int num4 = random.Next(image.Height);
                    int num5 = random.Next(image.Height);
                    graphics.DrawLine(new Pen(Color.Black), num2, num4, num3, num5);
                }
                Font font = new Font("Arial", 12f, FontStyle.Italic | FontStyle.Bold);
                LinearGradientBrush brush = new LinearGradientBrush(new Rectangle(0, 0, image.Width, image.Height), Color.Blue, Color.DarkRed, 1.2f, true);
                graphics.DrawString(ValidateCode, font, brush, (float)2f, (float)2f);
                for (int j = 0; j < 100; j++)
                {
                    int x = random.Next(image.Width);
                    int y = random.Next(image.Height);
                    image.SetPixel(x, y, Color.FromArgb(random.Next()));
                }
                graphics.DrawRectangle(new Pen(Color.Silver), 0, 0, image.Width - 1, image.Height - 1);
                MemoryStream stream = new MemoryStream();
                image.Save(stream, ImageFormat.Gif);
                base.Response.ClearContent();
                base.Response.ContentType = "image/Gif";
                base.Response.BinaryWrite(stream.ToArray());
            }
            finally
            {
                graphics.Dispose();
                image.Dispose();
            }
        }

        private string GenerateValidateCode()
        {
            Random random = new Random();

            char[] chars = "0123456789".ToCharArray();
            System.Text.StringBuilder myStr = new System.Text.StringBuilder();

            for (int i = 0; i < 4; i++)
            {
                myStr.Append(chars[random.Next(chars.Length)]);
            }
            string text = myStr.ToString();

            this.Session.Add("ValidateCode", text);
            return text;
        }
        #endregion    
    }

}
``