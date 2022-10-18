import argparse

def main():
    
    parser = argparse.ArgumentParser(description='Create some strings')
    parser.add_argument('-s','--string', nargs='+',
                    help='The input strings')
    parser.add_argument('-l', '--lengths', type=int, nargs='+',
                    help='The amount of times the string will be concatanated')

    args = parser.parse_args()

    if(args.string and args.lengths):
        if(len(args.string) == len(args.lengths)):
            for i in range(len(args.string)):
                for _ in range(int(args.lengths[i])):
                    print(args.string[i], end="")
            print("")
        else:
            print("Missmatch between amount of strings and amount of 'number of times to print'")
    else:
        print("Missing arguments...")

if __name__ == "__main__":
    main()